package net.zhenglai
package basic

class Applicable {
  def apply(x: Int) = x + 1
}

@main def hof1(): Unit = {
  val app = new Applicable
  // The apply method is treated in a special way
  // The apply method allows instances of classes to be “invoked” like functions
  println ( app.apply ( 2 ) )
  println ( app ( 2 ) )
}

// built-in types for function objects, which are nothing else but plain instances with apply methods
// “functions” are nothing but objects with apply methods
@main def builtinft() = {
  // Int => Int, which is also another sweet name for Function1[Int, Int].
  val incr: Int => Int = new Function1[Int, Int] {
    override def apply(v1 : Int) : Int = v1 + 1
  }
  println ( incr ( 1 ) )
}

// The functions which take other functions as arguments and/or return other functions as results are called HOFs, or higher-order functions.
def ntimes(f: Int => Int, n: Int): Int => Int =
  if (n <= 0) (x: Int) => x
  else (x: Int) => ntimes(f, n-1)(f(x))

// ntimes(f, 4) = x => nTimes(f, 3)(f(x)) = f(f(f(f(x))))

// oop style
// Being recursive, these objects are short lived, so even though they might be using more memory than necessary, they will be quickly freed by the JVM’s garbage collection.
def ntimesfull(f: Function1[Int, Int], n: Int): Function1[Int, Int] =
  if (n <= 0) new Function[Int, Int] { override def apply(x: Int) = x }
  else new Function[Int, Int] {
    override def apply(v1 : Int) : Int = ntimesfull ( f, n - 1 ).apply ( v1 )
  }

@main def hofm() = {
  println ( ntimes ( _ * 2, 3 ) )
}