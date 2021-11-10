package net.zhenglai

object p31 {
  def isPrime(n: Int): Boolean = if (n < 2) false else {
    !(2 to Math.sqrt(n).toInt).exists(n % _ == 0)
  }

  def primes(): LazyList[Int] = {
    // LazyList.from(1).filter(isPrime)
    LazyList.cons(2, LazyList.from(3, 2)).filter(isPrime)
  }

  def main(args: Array[String]): Unit = {
    println(isPrime(7))
    println(isPrime(2))
    println(isPrime(1))
    println(isPrime(9))
    println(isPrime(11))
    println(isPrime(13))
    println(primes().take(30).toList)
  }
}
