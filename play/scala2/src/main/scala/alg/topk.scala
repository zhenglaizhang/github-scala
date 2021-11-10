package net.zhenglai
package alg

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpMethods, HttpRequest}
import akka.util.ByteString
import cats.data.{NonEmptyList, Validated}
import java.io.Closeable
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.io.Source
import scala.util.matching.Regex
import scala.util.{Failure, Success, Try}

final case class Movie(id: String, rating: Double)

object using {
  def apply[A <: Closeable, B](s: => A)(f: A => B): B = {
    var source: Option[A] = None
    try {
      source = Some(s)
      f(source.get)
    } finally {
      source.foreach(_.close())
    }
  }
}

object TopK {
  val linePattern: Regex = """^([\w\s]+),([\d.]+)$""".r

  def lineToMovieValidated(line: String): Validated[NonEmptyList[String], Movie] = {
    line match {
      case linePattern(id, rating) => {
        val r = Try(rating.toDouble)
        r match {
          case Success(rat) => Validated.valid(Movie(id, rat))
          case Failure(ex) => Validated.invalidNel(s"Parsing rating error: $ex, line: $line")
        }
      }
      case _ => Validated.invalidNel(s"Parsing error: $line")
    }
  }

  def lineToMovie(line: String): Option[Movie] = {
    line match {
      case linePattern(id, rating) => {
        println("matched")
        rating.toDoubleOption.map(Movie(id, _))
      }
      case _ => None
    }
  }

  def topk1(file: String, k: Int): Seq[Movie] = {
    var s: Option[Source] = None
    try {
      s = Some(Source.fromFile(file))
      s.get.getLines()
        .drop(1) // drop header
        .flatMap(lineToMovie)
        .toVector
        .sortWith(_.rating > _.rating)
        .take(k)
    } finally {
      for {
        source <- s
      } source.close()
    }
  }

  def topk2(file: String, k: Int): Seq[Movie] = {
    using(Source.fromFile(file)) { s =>
      var topk = Vector.empty[Movie]
      s.getLines()
        .drop(1)
        .flatMap(lineToMovie)
        .foreach { m =>
          topk = if (topk.length < k) (topk :+ m).sortWith(_.rating < _.rating)
          else if (topk.head.rating >= m.rating) topk
          else (topk.tail :+ m).sortWith(_.rating < _.rating)
        }
      topk
    }
  }

  def topk4(url: String, k: Int): Seq[Movie] = {
    implicit val system = ActorSystem(Behaviors.empty, "topk4")
    implicit val ec = system.executionContext
    val f: Future[Vector[Movie]] = Http().singleRequest(HttpRequest(uri = url, method = HttpMethods.GET))
      .flatMap(_.entity.dataBytes.runFold(ByteString.empty)(_ ++ _))
      .map(_.utf8String.split("\n"))
      .map { lines =>
        lines.drop(1)
          .flatMap(lineToMovie)
          .toVector
          .sortWith(_.rating > _.rating)
          .take(k)
      }
    Await.result(f, 10 seconds)
  }

  def main(args: Array[String]): Unit = {
    //    println(topk1("play/scala2/src/main/scala/alg/movies.csv", 3))
    //    println(topk2("play/scala2/src/main/scala/alg/movies.csv", 3))
    //    println(topk4("https://gist.githubusercontent" +
    //      ".com/zhenglaizhang/e44992fbe1a6cdeeeed76186ea39aee5/raw/28ac065ffc7e328f0acda327999a642f5232be48/movies
    //      .csv", 3))
    val l = """hello abc,123.3"""
    val m = linePattern.findAllMatchIn(l).toList
    println(m)
    val r = lineToMovie(l)
    println(r)
  }
}
