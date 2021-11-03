package net.zhenglai
package akkaz.httpclient

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding.{Get, Post}
import akka.http.scaladsl.model._
import akka.util.ByteString
import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}
//import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object SimpleHttpClient {
  val hr1 = HttpRequest(uri = "https://akka.io")
  val hr2 = Get("https://akka.io")
  val hr3 = Get("https://akka.io?foo=bar")
  val hr4 = HttpRequest(
    method = HttpMethods.GET,
    uri = "https://api.github.com/orgs/wearexteam/repos",
  )
  val hr5 = HttpRequest(
    method = HttpMethods.POST,
    uri = "https://akka.io",
    entity = HttpEntity(ContentTypes.`text/plain(UTF-8)`, "data")
  )

  def hr6(implicit ec: ExecutionContext) = Post("https://akka.io", "data")

  case class User(login: String)

  case class Repo(name: String, full_name: String, owner: User)

  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
  import akka.http.scaladsl.unmarshalling.Unmarshal
  import spray.json.DefaultJsonProtocol._

  implicit val userFormat = jsonFormat1(User)
  implicit val repoFormat = jsonFormat3(Repo)

  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "SimpleHttpClient")
    implicit val ec: ExecutionContext = system.executionContext
    // stream based API
    Http().singleRequest(hr1)
      .flatMap(_.entity.dataBytes.runFold(ByteString(""))(_ ++ _))
      .map(_.utf8String)
      .foreach(println)

    val response: Future[HttpResponse] = Http().singleRequest(hr4)
    val result: Future[List[Repo]] = response.flatMap(resp => Unmarshal(resp).to[List[Repo]])
    val r = Await.result(result, 5 seconds)
    println(r)
    Http().singleRequest(hr1)
      .onComplete {
        case Success(res) => println(res)
        case Failure(ex) => sys.error(ex.getMessage)
      }


    // future based api
    Http().singleRequest(hr4)
      .flatMap(resp => Unmarshal(resp).to[List[Repo]])
      .map(println)
      .foreach(_ => system.terminate())
  }
}
