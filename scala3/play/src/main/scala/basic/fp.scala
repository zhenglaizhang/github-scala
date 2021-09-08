package net.zhenglai
package basic

@main def lazyList() = {
  val x = LazyList.from(1, 2)
  println(x)
  val y = x.take(10).toList
  println(y)
}

@main def curry(): Unit = {
  def mcat(s1: String, s2: String): String = s1 + s2
  val mcatCurried = mcat.curried
  val fcat = (s1: String, s2: String) => s1 + s2
  val factCurried = fcat.curried
  mcat("hello", "world")
  fcat("hello", "world")
  mcatCurried("hello")("world")
  factCurried("hello")("world")

  val hmcat : String => String = mcat.curried("hello")
  val hfcat : String => String =  fcat.curried("hello")
}

@main
def tuple(): Unit = {
  def mult(d1: Double, d2: Double): Double = d1 + d2
  mult(1.1, 2.1)
  val ft1 : ((Double, Double)) => Double = Function.tupled(mult)
  val ft2: ((Double, Double)) => Double = mult.tupled
  ft1((1.1, 2.1))
  ft2((1.1, 2.1))

  val ft3 = Function.untupled(ft1)
  ft3(1.1, 1.2)
}


