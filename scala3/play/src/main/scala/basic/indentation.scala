import scala.Ordering
import java.lang.System

// If Expressions
@main def ifexpression(): Unit = {
  val scala2c = 
    if (2 > 3) "bigger"
    else "smaller"

  // indentation becomes significant
  val scala3 = 
    if 2 > 3 then
      "bigger"
    else 
      "smaller"

  val scala3shorter = if 2 > 3 then "bigger" else "smaller"
}

// For Comprehensions
@main def forcomprehension(): Unit = {
  for 
    n <- List(1, 2, 3)
    c <- List('a', 'b', 'c')
  yield s"$c$n"
}

// Indentation regions
def somemethod(year: Int): Int = 
  println("wow")

  43
// imagine the compiler inserted braces between = and your returned value; in this way, the implementation of the method is a code block, which, obviously, is a single expression whose value is given by its last constituent expression. The significant indentation means, in this case, that we actually have an invisible code block there

// An indentation region is also created when we define classes, traits, objects or enums 
// followed by a colon : and a line break. 
// This token is now interpreted by the compiler as “colon at end of line”, 
// which is to say “colon then define everything indented”.

// Scala 3 introduced the end token to differentiate which code belongs to which indentation region
// The `end` token will definitely prove useful when we have 100x more code for below Animal definition
// Come to take a more structured approach, it’s not that bad, and it might actually help
class Animal:
  def eat(): Unit =
    if System.currentTimeMillis() % 2 == 0 then
      println("even")
    else 
      println("odd")
    end if 
  end eat 
end Animal

trait Wow:
  def eat(animal: Animal): Unit 

object MeowW: 
  def apply(name: String): Wow = ???

given myOrder: Ordering[Int] with
  def compare(x: Int, y: Int) = 
    if x < y then 1
    else if x > y then -1 
    else 0

// indent with spaces or tabs? 
// Scala 3 supports both, and the compiler is able to compare indentations.
// Here’s the bottom line: just don’t mix spaces with tabs. Pick your camp, fight to the death, but don’t mix ‘em.

// Function arguments 
@main def wow1(): Unit = {
  import scala.concurrent.Future 
  import concurrent.ExecutionContext.Implicits.global
  // This brace syntax is applicable to any method with a single argument
  val x = Future { 1 }

  // We could add support for it with the -Yindent-colons compiler option, which would allow us to add an end-of-line : and write the method argument indented
  // val y = Future:
  //   2021

  // List(1,2,3).map:
  //   x => x + 1
}
