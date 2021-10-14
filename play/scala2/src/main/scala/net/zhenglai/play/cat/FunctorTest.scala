package net.zhenglai
package net.zhenglai.play.cat

import cats.Functor

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}
import scala.util.Random

// Pure functional programming deals with immutable values,
// and so if we want to transform a data structure, we’ll need to create another one
// `map` concept is transferable, and it bears the name of Functor
object FunctorCool {
  // In Scala, a “transferable concept” can be easily expressed as a type class.
  trait Functor[M[_]] {
    def map[A, B](ma : M[A])(f : A => B) : M[B]
  }
  object Functor {
    implicit val listFunctor : Functor[List] = new Functor[List] {
      override def map[A, B](ma : List[A])(f : A => B) : List[B] = ma.map(f)
    }
    implicit val treeFunctor : Functor[Tree] = new Functor[Tree] {
      override def map[A, B](ma : Tree[A])(f : A => B) : Tree[B] = ma match {
        case Branch(value, left, right) => Branch(f(value), map(left)(f), map(right)(f))
        case Leaf(value) => Leaf(f(value))
      }
    }
  }
  // Attaching the “Mappable” Concept
  object FunctorSyntax {
    implicit class FunctorOps[M[_], A](ma : M[A]) {
      def map[B](f : A => B)(implicit functor : Functor[M]) : M[B] = functor.map(ma)(f)
    }
  }
  def do10x[M[_]](ma : M[Int])(implicit functor : Functor[M]) : M[Int] = functor.map(ma)(_ * 10)

  def main(args : Array[String]) : Unit = {
    val _ = do10x(List(1, 2, 3))
    val tree = Tree.branch(
      1,
      Tree.branch(2,
        Tree.leaf(3),
        Tree.leaf(4)
      ),
      Tree.leaf(5)
    )
    // This is the power of a Functor: it allows us to generalize an API and process any “mappable” data structures
    // in a uniform way, without needing to repeat ourselves.
    val r = do10x(tree)
    println(r)
    Functor.treeFunctor.map(tree)(println)
    import FunctorSyntax._
    println(tree.map(_ * 2))
  }
  sealed trait Tree[+A]
  case class Leaf[+A](value : A) extends Tree[A]
  case class Branch[+A](value : A, left : Tree[A], right : Tree[A]) extends Tree[A]
  object Tree {
    def leaf[A](value : A) : Tree[A] = Leaf(value)
    def branch[A](value : A, left : Tree[A], right : Tree[A]) : Tree[A] = Branch(value, left, right)
  }
}
class FutureNotRT {
  val fut1 = {
    val r = new Random(0L)
    val x = Future(r.nextInt())
    for {
      a <- x
      b <- x
    } yield (a, b)
  }
  val fut2 = {
    val r = new Random(0L)
    for {
      a <- Future(r.nextInt())
      b <- Future(r.nextInt())
    } yield (a, b)
  }
  def main(args : Array[String]) : Unit = {
    val r1 = Await.result(fut1, 1.second)
    val r2 = Await.result(fut2, 1.second)
    println(r1)
    println(r2)
  }
}
object FunctorTest {

  import cats.instances.function._
  import cats.syntax.functor._

  def doMath[F[_]](start : F[Int])(implicit functor : Functor[F]) : F[Int] =
    start.map(_ + 1 * 2)
  def main(args : Array[String]) : Unit = {
    val f1 : Int => Double = _.toDouble
    val f2 : Double => Double = _ * 2
    println((f1 map f2) (1))
    println((f1 andThen f2) (1))
    println(f2(f1(1)))
    val liftedF1 = Functor[Option].lift(f1)
    println(liftedF1(Some(1)))
    println(liftedF1(None))
    Functor[Seq].as(Seq(1, 2, 3), "As")
    // TODO: play other Functor methods

    println(doMath(Option(20)))
    println(doMath(List(1, 2, 3)))
  }
}

// Functors embody the concept of “mappable” data structures. In Scala, we generally write it as a type class,
// because we’d like to attach this concept to some data structures (e.g. lists, options, binary trees) but not
// others. We use Functors to generalize our APIs, so that we don’t have to write the same transformations on
// different data structures. After creating Functor instances, we can even add the map extension method to the data
// structures we would like to support, if it doesn’t have it already.