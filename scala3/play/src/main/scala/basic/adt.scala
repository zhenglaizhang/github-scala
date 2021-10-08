package net.zhenglai
package basic
// Algebraic Data Types
//  - ADTs are a data type
//  - We can use them to structure the data used by a program.
//  - We can define ADT as a pattern because they give us a standard solution to modeling data

//  - Whereas in OOP, we tend to think about data and functionality together
//  - in FP, we use to define data and functionality separately.
//  - Sum types and Product types give us the right abstractions to model domain data.

// Sum Types
sealed trait Weather
case object Sunny extends Weather
case object Windy extends Weather
case object Rainy extends Weather
case object Cloudy extends Weather
case object Foggy extends Weather

// type Weather = Sunny + Windy + Rainy + Cloudy + Foggy
// the Weather type is Sunny OR Windy OR Rainy, and so forth (to tell the truth, we are talking about an XOR, or exclusive OR). Because a Sum type enumerates all the possible instances of a type, they are also called Enumerated types

// the word sealed forces us to define all possible extensions of the trait in the same file, allowing the compiler
// to perform “exhaustive checking” when pattern matching

// If we want to discourage trait mixing and get better binary compatibility, we can use a sealed abstract class
// instead of a sealed trait.
// Using a case object instead of a simple object gives us a set of useful features, such as the unapply method, which lets our objects to work very smoothly with pattern matching, the free implementation of the methods equals, hashcode, toString, and the extension from Serializable

// Product Types
case class ForecastRequest(val latitude: Double, val longitude: Double)
// we can write the constructor as (Long, Long) => ForecastRequest
// the number of possible values of ForecastRequest is precisely the cartesian product of the possible values for the latitude property AND all the possible values for the longitude property
// type ForecastRequest = Long x Long
// Product types introduce a lot more flexibility in modeling and representing reality. However, this comes within a
// cost: The more the type’s values, the more cases we will need to verify in tests.

// Product types are associated with the AND operator, in contrast with the Sum types that we associate with the OR operator.
//  Scala models Product types using a case class. As for case object, one of the power of case classes is the free implementation of the unapply method, which allows smoothly decomposing the Product types

// Hybrid Types

sealed trait ForecastResponse
case class Ok(weather: Weather) extends ForecastResponse

// simpler modeling
// val (error, description): (String, String) = ("PERMISSION_DENIED", "You have not the right permission to access
// the resource")
case class Ko(error: String, description: String) extends ForecastResponse
// - The ForecastResponse is a Sum type because it is an Ok OR a Ko.
// - The Ko type is a Product type because it has an error AND a description
// - Hybrid types are also called Sum of Product types

@main def testadt() : Unit = {
  // TODO: fix it
//  val (lat, long): (Double, Double) = ForecastRequest(1.0D, 12.1D)
//  println(lat)
//  println(long)
}

// Using a tagged type, we can add a name to the entity that maps to our domain model. Moreover, using a name lets the compiler automatically validate the information, generating only valid combinations. There is a principle in data modeling that illegal states should not be representable at all, and ADTs allow us to do exactly that
// The compositionality of ADTs is the crucial feature that makes them a perfect fit to model domain models in applications. Indeed, functional programming lets us build larger abstractions from smaller ones
// - ADTs’ representation in Scala through case class and case object forces us to use immutable types
// - ADTs work very well with pattern matching and partial functions
// - For Akka developers, the best practice is to design the communication protocols between actors (i.e., request and response messages) using ADTs
// - ADTs provide no functionality, only data. So, ADTs minimize the set of dependencies they need, making them easy to publish and share with other modules

// An algebra is nothing more than a type of objects and one or more operations to create new items of that type
//  -  in numeric algebra the objects are the numbers, and the operations are +, -, *, and /. Talking about ADTs, the objects are the Scala standard types themselves, and the class constructors of ADTs are the operators. They allow building new objects, starting from existing ones.


// we can introduce more mathematics associated with ADTs. In detail, we have:
//  - case classes are also known as products
//  - sealed traits (or sealed abstract classes) are also known as coproducts
//  - case objects and Int, Double, String (etc) are known as values

// we can compose new data types from the AND and OR algebra. A Sum type (or coproduct) can only be one of its values:
//  - Weather (coproduct) = `Sunny` XOR `Windy` XOR `Rainy` XOR ...
//Whereas a Product type (product) contains every type that it is composed of:
//  - Ko (product) = String x String

// We can define the complexity of a data type as the number of values that can exist. Data types should have the least amount of complexity they need to model the information they carry.
case class ConfigBad(a: Boolean, b: Boolean, c: Boolean)
// a complexity of 8
sealed trait Config
case object A11 extends Config
case object B11 extends Config
case object C11 extends Config
// The Sum type Config has the same semantic as its Product type counterpart, plus it has a smaller complexity, and it does not allow 5 invalid states to exist. Also, as we said, the lesser values a type admits, the easier the tests associated with it will be. Less is better
//Let’s rethink our Ko Product type. Can we do any better? Absolutely. We should try to avoid invalid states and limit the number of values the type can admit. Hence, we can replace the first value of the type, error: String, with a Sum type that enumerates the possible types of available errors:

sealed trait Error
case object NotFound extends Error
case object Unauthorized extends Error
case object BadRequest extends Error
case object InternalError extends Error
// And so on...
case class KoImproved(error: Error, description: String) extends ForecastResponse
//We reduced the complexity of the type, making it more focused, understandable, and maintainable.