package net.zhenglai

object p28 {
  def lsort[A](xs: List[List[A]]): List[List[A]] = {
    xs.sortBy(_.length)
  }

  def main(args: Array[String]): Unit = {

  }
}