package net.zhenglai
package basic

// What a method is and how to define a function value (lambda)
// eta-expansion and partially-applied functions
// Methods and functions are different things in Scala
// Because the user sees and uses them in the same way (just invoke them), they’re “morally” equivalent.
// The eta-expansion mechanism allows the conversion between a method and a function.
// Eta-expansion turns a method into a function which will take the remaining argument lists (however large) in turn, however long the chain may be
object PartiallyAppliedFunctionEtaExpansion {
  // It's a method, which is a piece of code that can be invoked on an instance of a class.
  // A method is a member of the enclosing class or object
  // A method is tied to some instance of a class
  def incrMethod(x: Int): Int = x + 1
  val three = this.incrMethod(2)

  // Function values (aka lambdas) are pieces of code that can be invoked independently of a class or object.
  // Functions are assignable to values or variables, can be passed as arguments and can be returned as results
  // These function values are actually instances of the FunctionN family of traits with an apply method which benefits from special treatment
  val incrFunction = (x: Int) => x + 1
  val three2 = incrFunction(2)

  // What the compiler does
  val incrFunctionInternal = new Function[Int, Int] {
    override def apply(x: Int): Int = x + 1
  }
  val threeInternal = incrFunctionInternal.apply(2)

  // Method -> Function
  // Because a method and a function are seen differently by the JVM - a method of a class vs a field of type FunctionN

  // compiler will think you try to call your increMethod, which requires arguments
  // val incrementF = incrMethod
  // The syntax `<function> _` is no longer supported;
  //  you can simply leave out the trailing ` _`
  // val incrF = incrMethod _
  val incrF = incrMethod
  // The underscore at the end is a signal for the compiler that you want to turn the method into a function value,
  // This conversion is called eta-expansion, and the compiler will generate a piece of code that will look something like
  val incrFInternal = (x: Int) => incrMethod(x)

  // The compiler can also do this automatically if you give it the function type in advance
  // The compiler can disambiguate the context, because you declared that you want a function so the compiler will automatically eta-expand the method for you
  val incrF2: Int => Int = incrMethod

  def add(x: Int, y: Int): Int = x + y
  // The eta-expanded function value (lambda)
  // val addF: (Int, Int) => Int = add _
}

// Partially Applied Functions
object PAF {
  def multiArgAdder(x: Int)(y: Int): Int = x + y
  def add2 = multiArgAdder(2) // _
  val three = add2(1)
  // The compiler can detect whether a value is expected to have a function type, and so it can automatically eta-expand a method for you
  List(1, 2, 3).map(multiArgAdder(3)) // eta-expansion is done automatically

  def threeArgAdder(x: Int)(y: Int)(z: Int): Int = x + y + z
  val wow: Int => Int => Int = threeArgAdder(2) // _
  val ten = wow(3)(5)

  val oneArgRemaining: Int => Int = threeArgAdder(2)(3) // _
}
