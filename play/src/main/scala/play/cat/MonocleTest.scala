package net.zhenglai
package play.cat
import monocle.syntax.all._

class MonocleTest {
  def main(args: Array[String]): Unit = {
    val user = User("uname", 12, Address("high", 2))
    val newUser = user.focus(_.address.streetName).modify(_.toUpperCase)
    println(newUser)
    val x = user.focus(_.address.streetName).get
    println(x)
  }

}
