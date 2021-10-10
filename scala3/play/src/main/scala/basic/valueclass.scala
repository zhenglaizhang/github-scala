package net.zhenglai
package basic

// Unit is an AnyVal type, but involves no storage at all
//  - Java void is a keyword
//  - Unit is a real type with one literal value () and we rarely use that value explicitly
//  - Unit behaves like a tuple with zero elements ()
//  - Name unit comes from algegra, where adding the unit value to any value returns the original value

// One of the main rules of functional developers is that we should always trust a function’s signature. Hence, when
// we use functional programming, we prefer to define ad-hoc types to represent simple information such as an
// identifier, a description, or a currency. This leads to Value Class

// The compiler cannot warn us of our errors because we represent both pieces of information, i.e. the code and the
// description, using simple Strings. This fact can lead to subtle bugs, which are very difficult to intercept at
// runtime as well.

object vcold {
  trait Product

  case class BarCode(code: String)

  case class Description(txt: String)

  trait AnotherProductRepository {
    def findByCode(barCode: BarCode): Option[Product]

    def findByDescription(description: Description): List[Product]
  }

  // It is not possible anymore to search a product by code while accidentally passing a description
  // However, we can still create a BarCode using a String representing a description:
  val aFakeBarCode: BarCode = BarCode("I am a real description")
  // To overcome this issue we must use the smart constructor design pattern

  sealed abstract class BarCodeWithSmartCtor(code: String)

  object BarCodeWithSmartCtor {
    // we are using a class to wrap Strings, the compiler must instantiate a new BarCode and Description every single
    // time. The over instantiation of objects can lead to a problem concerning performance and the amount of
    // consumed memory.
    def mkBarCode(code: String): Either[String, BarCodeWithSmartCtor] =
      Either.cond(
        code.matches("\\d-\\d{6}-\\d{6}"),
        new BarCodeWithSmartCtor(code) {},
        s"The given $code has not the right format"
      )
  }

  val theBarCode: Either[String, BarCodeWithSmartCtor] =
    BarCodeWithSmartCtor.mkBarCode("8-000123-123148")
}

object vc {
  // An Idiomatic Approach
  // Idiomatic value classes avoid allocating runtime objects and the problems we just enumerated.
  // A idiomatic value class is a class (or a case class) that extends the type AnyVal, and declares only one single
  // public val attribute in the constructor. Moreover, a value class can declare def:
  case class BarCodeValueClass(val code: String) extends AnyVal {
    def countryCode: Char = code.charAt(0)
  }
  // However, value classes have many constraints: They can define def, but not val other than the constructor’s
  // attribute, cannot be extended, and cannot extend anything but universal traits (for the sake of completeness, a
  // universal trait is a trait that extends the Any type, has only def as members, and does no initialization).

  // The main characteristic of a value class is that the compiler treats it as a case class at compile-time. Still,
  // at runtime, its representation is equal to the type declared in the constructor. Roughly speaking, the
  // BarCodeValueClass type is transformed as a simple String at runtime.
  //
  // Hence, due to the lack of runtime overhead, value classes are a valuable tool used in the SDK to define extension
  // methods for basic types such as Int, Double, Char, etc

  // the following use cases that need an extra memory allocation:
  //  - A value class is treated as another type, the first rule’s concrete case also concerns using a value class as
  //    a type argument
  //  - A value class is assigned to an array.
  //  - Doing runtime type tests, such as pattern matching.
}

@main def upperbounds() = {
  val xs: Seq[Float] = Seq(1, 1.1f)
  val xs2: Seq[Double] = Seq(1.1f, 1.1)
  val xs3: Seq[AnyVal] = Seq(true, 1)
  val xs4: Seq[Int] = Seq(1, 'a')
}

// The NewType library allows us to create new types without the overhead of extra runtime allocations, avoiding the
// pitfalls of Scala values classes