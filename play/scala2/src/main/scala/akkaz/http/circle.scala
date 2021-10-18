package net.zhenglai
package akkaz.http

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import java.util.UUID

// Circe library is very popular within the Typelevel ecosystem
// Add the FailFastCirceSupport trait as a mix-in to your main application object. This will bring the necessary
// implicits so that the directives can find the marshallers (serializers) between the HTTP entities that Akka HTTP
// understands and the internal formats of Circe
object AkkaHttpCirceJson extends FailFastCirceSupport {

  // Add an import so that Circe can automatically generate an implicit encoder/decoder pair for the types you want
  // to support (case classes, usually)

  import io.circe.generic.auto._

  implicit val system = ActorSystem(Behaviors.empty, "AkkaHttpJson")
  val route: Route = (path("api" / "user") & post) {
    entity(as[Person]) { person =>
      complete(UserAdded(UUID.randomUUID().toString, System.currentTimeMillis()))
    }
  } : @annotation.nowarn("cat=lint-byname-implicit")

  def main(args: Array[String]): Unit = {
    val _ = Http().newServerAt("localhost", 8080).bind(route)
  }
}