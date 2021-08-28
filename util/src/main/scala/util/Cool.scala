package net.zhenglai
package util

object Cool {
  implicit class Debuggable[A](a: A) {
    def p(): Unit = println(a)
  }

  implicit class FunChainable[A](a: A) {
    def pipe[B](f: A => B): B = f(a)
    def |>[B](f: A => B): B = pipe(f)
  }

  def foldMap[A, B](xs: Seq[A])(f: A => B): B = ???
}
