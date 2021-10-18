package net.zhenglai
package akkaz.http

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import java.util.UUID

final case class Person(name: String, age: Int)

final case class UserAdded(id: String, timestamp: Long)

// spray json

import spray.json._

// Create a scope - a trait, an object etc - which has JSON converters for any type that you might want to support.
trait PersonProtocol extends DefaultJsonProtocol {
  implicit val personFormat: RootJsonFormat[Person] = jsonFormat2(Person)
  implicit val userAddedFormat: RootJsonFormat[UserAdded] = jsonFormat2(UserAdded)
}

// Add the implicit JSON converters into the scope of your route. If you created the converters within a trait, as //
// we did above, you’ll simply need to mix the trait into your main app. If you wrote them inside an object, you’ll
// need to import the contents of that object. Besides this, you’ll also need to mix-in the trait SprayJsonSupport,
// which contains some implicit definitions that the Akka HTTP server directives will need
object AkkaHttpJson extends PersonProtocol with SprayJsonSupport /*convert between an Akka HTTP Entity and the internal JSON format of Spray-json*/ {
  implicit val system = ActorSystem(Behaviors.empty, "AkkaHttpJson")
  val route: Route = (path("api" / "user") & post) {
    // `as[Person]` fetches whatever implicit marshaller (serializer) of Persons the compiler has access to.
    entity(as[Person]) { person =>
      complete(UserAdded(UUID.randomUUID().toString, System.currentTimeMillis()))
    }
  }

  def main(args: Array[String]): Unit = {
    val _ = Http().newServerAt("localhost", 8080)
      .bind(route)
  }
}