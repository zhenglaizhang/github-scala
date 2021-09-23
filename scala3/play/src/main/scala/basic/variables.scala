package net.zhenglai
package basic

// How Things Don’t Make Sense - Scala Variables
// “don’t use variables, they are discouraged”
object variables {
  val aval = 2
  // aval = 3
  // error: Reassignment to val aval

  var avar = 1
  avar = 2

  // Because FP
  // people start thinking - or I should say continue thinking - procedurally.
  // we want think in terms of expressions, not instructions.

  var i = 0
  while (i < 10) {
    println("wow in looping")
    i = i + 1
  }

  (0 until 10).foreach(_ => println("doing it right now"))

  // Mutation is also a big problem:
  // Reusing variables makes code very hard to read and reason about, especially in multithreaded or distributed applications.
}
// Rule of thumb:
//  * If you’re learning Scala, pretend you’ve never even heard of variables. There are no such things.
//  * If you’re teaching Scala, if you don’t want your students to use variables and loops, don’t teach them!
