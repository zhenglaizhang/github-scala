package net.zhenglai

object gcd {
  def gcd(a: Int, b: Int): Int = {
    if (b == 0) a
    else gcd(b, a % b)
  }

  def main(args: Array[String]): Unit = {
    println(gcd(36, 63))
  }
}
