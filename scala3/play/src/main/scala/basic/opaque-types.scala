// Background and Motivation
// We define new types as wrappers over existing types (composition) => overhead
// One of the fundamental pieces of data is a user’s details,
// but we want to enforce some rules so that the user’s details are correct, e.g. name start with a capital letter
object w11 {
  case class Name(value: String) {}
}
// This new Name type incurs some sort of overhead. If we have millions of users, these tiny overheads will start to add up


// Opaque Types
// Opaque types allow us to define Name as being a String, and allows us to also attach functionality to it
object SocialNetwork {
  // We’ve defined a type alias, which we can now freely use interchangeably 
  // with String inside the scope it’s defined
  // The idea with an opaque type is that you can only interchange it with String in the scope it’s defined, but otherwise the outside world has no idea that a Name is in fact a String
  opaque type Name = String

  // The API for the new type will have to be defined as extension methods
  extension (n: Name) {
    def length: Int = n.length
  }

  object Name {
    def fromString(s: String): Option[Name] = 
      if (s.isEmpty || s.charAt(0).isLower) None else Some(s)
  }
}

// To the outside scope, Name is a completely different type with its own API (currently none). This allows you to start with a new type being implemented in terms of an existing type (String) with zero boilerplate or overhead. On the other hand, the new type is treated as having no connection to the type it’s implemented as
// The bad news is that this new type has no API of its own
// The good news is that you now have a fresh zero-overhead type whose API you can write from scratch
@main def testopaque(): Unit = {
  import SocialNetwork.*
  // val name: Name = "hello"
  val name: Option[Name] = Name.fromString("hello")
  name.map(_.length)
}

// Opaque Types with Bounds
// Opaque type definitions can have type restrictions, much like regular type aliases
object Graphics {
  opaque type Color = Int 
  opaque type ColorFilter <: Color = Int
  val Red: Color = 0xff000000
  val Green: Color = 0xff0000
  val Blue: Color = 0xff00
  val halfTransparency: ColorFilter = 0x88
}
@main def opaquetypewithbounds(): Unit = {
  import Graphics.*
  case class Overlay(c: Color)
  val fadeLayer = Overlay(halfTransparency)
}