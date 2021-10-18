package net.zhenglai
package akkaz.http

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import de.heikoseeberger.akkahttpjackson.JacksonSupport
import java.util.UUID

// The usage is even simpler in this case: all you have to do is add the Jackson support trait to your main
// application object so that the compiler can build the implicits that the Akka HTTP directives will need to
// marshal/unmarshal entities to/from your types. The support trait is called (unsurprisingly) JacksonSupport
object AkkaHttpJackson extends JacksonSupport {
  implicit val system = ActorSystem(Behaviors.empty, "AkkaHttpJson")
  val route: Route = post {
    entity(as[Person]) { person =>
      complete(UserAdded(UUID.randomUUID().toString, System.currentTimeMillis()))
    }
  }

  def main(args: Array[String]): Unit = {
    val _ = Http().newServerAt("localhost", 8080).bind(route)
  }
}