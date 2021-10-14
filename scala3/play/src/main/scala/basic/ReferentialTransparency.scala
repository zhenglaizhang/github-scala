package net.zhenglai
package basic

// Why Should You Care About Referential Transparency
//  - a bit more language-agnostic as it applies to any programming language where FP can work
//  - referential transparency is a fancy term for “replaceable code”.
//  - A piece of code is referentially transparent if we can safely replace that piece of code with the value it
//    computes and vice-versa, anywhere where that piece is used, without changing the meaning or result of our program
//  -

object ReferentialTransparency {
  // This function is referentially transparent. Why? Because we can replace all occurrences of this function with the expression it evaluates to, and then with the value it computes, at any point in our program
  def add(a: Int, b: Int) = a + b

  val five = add(2, 3)
  val ten = five + five
  val ten_v2 = add(2, 3) + add(2, 3)
  val ten_v3 = 5 + add(2, 3)
  val ten_v4 = 10

  // This expression is not referentially transparent, because, besides the actual value the expression computes (the money you need to pay back), you also do something else (printing a respect line to the boss)
  def showMeTheMoney(money: Int): Int = {
    println("Here's your cash, good job!")
    money * 110 / 100
  }
  val a = showMeTheMoney(100)
  val b = showMeTheMoney(a)
  // This expression (the whatsTheTime function) is not referentially transparent
  // That’s because besides returning a value, this function interacts with some mutable state (the clock of the system).
  def whatsTheTime(): Long = System.currentTimeMillis()
}

object rt_benefits {
  // Referential Transparency Benefit #1: Refactoring
  //  - If we can determine that an expression is referentially transparent, we can quickly replace it with the value it produces, and vice-versa
  //  - A common pain in large codebasess is repeated code. With referentially transparent expressions, we can safely remove duplications
  //  - Referential transparency is basically a fancy term for “replaceable code”. Refactoring capabilities come for free in RT code

  // Referential Transparency Benefit #2: Mental Space
  def sumN(n: Int): Int =
    if (n <= 0) 0
    else n + sumN(n - 1)
  // If our function is RT, then we can quickly trace its execution
  // If our functions are not referentially transparent, then tracing the program execution is an order of magnitude harder
  // Using RT in our code frees mental space so that we can focus on what’s important, which is shipping quality software. Ideally, we can look at the type signature of a function and immediately be able to tell what that function computes and what it can do besides computing the values, which is why pure FP libraries like Cats Effect can be so powerful.
}

// Pure functional programming works with values, functions and expressions while those expressions are pure, meaning they only compute values and do not “do” anything besides computing values.
//  - Referential transparency describes the purity aspect of functional programming: only expressions that compute
//  values and don’t produce side effects of interacting with the world in any way.
//  - Referential transparency is a powerful mental tool in our programmers’ arsenal mainly because of their practical
//  utility: the ability to quickly inspect code, read, understand, reason about, change and deconstruct the meaning of our programs without altering it in any way
