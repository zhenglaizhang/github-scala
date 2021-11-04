package net.zhenglai
package akkaz.streamm

import akka.Done
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpMethods, HttpRequest}
import akka.stream.IOResult
import akka.stream.scaladsl.{FileIO, Source}
import akka.util.ByteString
import java.nio.file.Paths
import scala.concurrent.Future

object hellostream {
  // TODO: use which actorsystem?
  implicit val system = ActorSystem("stream")
  implicit val ec = system.dispatcher

  def s1(): Seq[Future[AnyRef]] = {
    val source = Source(1 to 10)
    val done: Future[Done] = source.runForeach(println)
    val factorials = source.scan(BigInt(1))(_ * _)
    val done2 = factorials.runForeach(x => println(s"fac: ${x}"))
    val done3: Future[IOResult] = factorials.map(n => ByteString(s"$n\n"))
      .runWith(FileIO.toPath(Paths.get("factorials.txt")))
    Seq(done, done2, done3)
  }

  val twitterTagsUrl = "https://gist.githubusercontent" +
    ".com/zhenglaizhang/27cba71e6c53a26b31b500b1e83af294/raw/4c2f42289e503e065aa09c64121713c77951192f/twitter-tags"

  final case class TwitterTags(author: String, tags: String, comments: Int)

  val twitterTagsPattern = """^([^,]+),(#\w+),(\d+)$""".r

  def twitterTags(url: String): Future[Any] = {
    Http().singleRequest(
      HttpRequest(
        method = HttpMethods.GET,
        uri = url))
      .flatMap(_.entity.dataBytes.runFold(ByteString(""))(_ ++ _))
      .map(_.utf8String)
      .map { s =>
        val twitterTags: Vector[TwitterTags] = s.split("\n")
          .toVector
          .collect {
            case twitterTagsPattern(author, tags, comments) =>
              comments.toIntOption
                .map(TwitterTags(author, tags, _))
          }.flatten
        twitterTags
      }
      .map(println)
      .map(_ => akka.Done)
  }

  def main(args: Array[String]): Unit = {
    Future.sequence(s1() :+ twitterTags(twitterTagsUrl))
      .onComplete(_ => system.terminate())
  }
}
