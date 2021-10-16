package net.zhenglai
package basic

import monocle.macros.GenLens

// Monocle is a popular library for traversing, inspecting and editing deeply nested data structures
// Lenses
//  - Monocle was invented because nested data structures are a pain to inspect and change
//  - Monocle gives us the capability to access a deeply nested field in a data structure, inspect it and/or change it
case class Guitar(make : String, model : String)
case class Guitarist(name : String, favoriteGuitar : Guitar)
case class RockBand(name : String, yearFormed : Int, leadGuitarist : Guitarist)
object LensTest {

  import monocle.Lens
  import monocle.macros.GenLens

  val metallica = RockBand(
    "Metallica",
    1981,
    Guitarist("Kirk Hammett", Guitar("ESP", "M II"))
  )
  val kirksFavGuitar = Guitar("ESP", "M II")
  val guitarModelLens : Lens[Guitar, String] = GenLens[Guitar](_.model)

  // inspecting
  guitarModelLens.get(kirksFavGuitar) // "M II"
  // modifying
  val formattedGuitar : Guitar =
    guitarModelLens.modify(_.replace(" ", "-"))(kirksFavGuitar)
  // The power of lenses becomes apparent when we compose those lenses
  val leadGuitaristLens = GenLens[RockBand](_.leadGuitarist)
  val guitarLens = GenLens[Guitarist](_.favoriteGuitar)
  val composedLens = {
    leadGuitaristLens.andThen(guitarLens).andThen(guitarModelLens)
  }
  val kirksGuitarModel2 : String = composedLens.get(metallica)
  val metallicaFixed2 = composedLens.modify(_.replace(" ", "-"))(metallica)
  // Why is this pattern called “lens”? Because it allows us to “zoom” into the deeply buried fields of data
  // structures, then inspect or modify them there.
}
object PrismsTest {
  sealed trait Shape
  case class Circle(radius : Double) extends Shape
  case class Rectangle(w : Double, h : Double) extends Shape
  case class Triangle(a : Double, b : Double, c : Double) extends Shape
  val aCircle = Circle(20)
  val aRectangle = Rectangle(10, 20)
  val aTriangle = Triangle(3, 4, 5)
  val shape : Shape = aCircle
  val newCircle : Shape = shape match {
    case Circle(r) => Circle(r + 20)
    case x => x
  }

  import monocle.Prism

  val circlePrism = Prism[Shape, Double] {
    case Circle(r) => Some(r)
    case _ => None
  }(r => Circle(r))
  // A Prism takes two argument lists, each of which takes a function. One is of type Shape => Option[Double], so a
  // “getter” (we return an Option because the Shape might be something other than a Circle). The other function is a
  // “creator”, of type Double => Shape. In other words, a Prism is a wrapper over a back-and-forth transformation
  // between a Double and a Shape. A prism allows us to investigate a Shape and get a double, or use a double and
  // create a Shape
  val circle : Shape = circlePrism(50)
  val noRadius : Option[Double] = circlePrism.getOption(aRectangle)
  val radius : Option[Double] = circlePrism.getOption(aCircle)
  // it clears a lot of boilerplate, for several reasons:
  //  - the prism’s apply method acts as a “smart constructor” which can instances of Circle for us
  //  - we can safely inspect any shape’s radius even if it’s not a Circle - this saves us the need to repeat the
  //  earlier pattern matching
}
// Why is this pattern called a “prism”? Because from the many types (facets) out of a hierarchy of data structures
// (prism), we’re interested in manipulating a single subtype (a “face”). Together with the lens pattern above and
// with a bunch of others, the Monocle library describes itself as an “optics” library for Scala.
object ComposeOptics {

  import PrismsTest._

  // We can inspect and/or modify nested data structures by combining the capability to zoom in (lens) and to isolate
  // a type (prism).
  case class Icon(background : String, shape : Shape)
  case class Logo(color : String)
  case class BrandIdentity(logo : Logo, icon : Icon)
  val iconLens = GenLens[BrandIdentity](_.icon)
  val shapeLens = GenLens[Icon](_.shape)
  // compose all
  val brandCircleR = iconLens.andThen(shapeLens).andThen(circlePrism)
  val aBrand = BrandIdentity(Logo("red"), Icon("white", Circle(45)))
  val enlargeRadius = brandCircleR.modify(_ + 10)(aBrand)
  // ^^ a new brand whose icon circle.scala's radius is now 55
  val aTriangleBrand = BrandIdentity(Logo("yellow"), Icon("black", Triangle(3, 4, 5)))
  brandCircleR.modify(_ + 10)(aTriangleBrand)
  // ^^ doesn't do anything because the shape isn't a triangle, but the code is 100% safe
  // All of the data access and manipulation is now reusable throughout the entire application!
}
