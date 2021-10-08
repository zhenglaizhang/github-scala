class Dog(name: String, age: Int)

object Dog {
  def stringToDog(line: String): Dog = {
    val tokens = line.split(",")
    Dog(tokens(0), tokens(1).toInt)
  }
}

implicit class DogInterpolator(sc: StringContext) {
  def dog(args: Any*): Dog = {
    // args: _* is deprecated
    val tokens = sc.s(args *).split(",")
    Dog(tokens(0), tokens(1).toInt)
  }
}

object custom_string_interpolator {

  @main def main(): Unit = {
    // S interpolator
    val pi = 3.141
    val sinterpolator = s"The value is $pi and half of it is ${pi / 2}"

    // Raw interpolator, which is the same as the S interpolator, except that it doesn’t escape characters, but keeps them exactly as they are:
    val rawinterpolator = raw"the value is $pi\n<- this is not a new line"

    // F interpolator, which has the ability to control the format in which values are shown
    val finterpolator = f"The approximate value of pi is $pi%3.2f"

    // customized interpolator
    // val query = sql"Select * from citizens where ..."
    val bob: Dog = Dog.stringToDog("bob,55")

    // (behind the scenes) invoke the “person” method from a new instance of PersonInterpolator created with the StringContext obtained by the compiler after parsing the string and isolating its “parts” and “arguments”.
    // Potential drawback: instantiation of the PersonInterpolator many times if you’re doing lots of these conversions.
    val name = "meow"
    val age = 44
    val meow: Dog = dog"$name,$age"
  }
}
