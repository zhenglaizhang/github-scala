package net.zhenglai
package akkaz.streamm

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpMethods, HttpRequest}
import akka.stream.scaladsl.{Broadcast, FileIO, Flow, GraphDSL, Keep, RunnableGraph, Sink, Source}
import akka.stream.{ClosedShape, IOResult, OverflowStrategy}
import akka.util.ByteString
import akka.{Done, NotUsed}
import cats.kernel.Monoid
import java.nio.file.Paths
import scala.concurrent.Future
import scala.concurrent.duration._

final case class Author(handle: String)

final case class HashTag(name: String)

final case class Tweet(author: Author, timestamp: Long, body: String) {
  def hashtags: Set[HashTag] = {
    val s = body.split(" ")
      .collect {
        case t if t.startsWith("#") => HashTag(t.replaceAll("[^#\\w]", ""))
      }.toSet
    println(s)
    s
  }
}

// Source[+Out, M1] => Flow[-In, +Out, M2] => Sink[-In, M3]
// Source[+Out, +Mat], Flow[-In, +Out, +Mat] and Sink[-In, +Mat]

object reusable {
  def count[A]: Flow[A, Int, NotUsed] = Flow[A].map(_ => 1)

  def sumSink: Sink[Int, Future[Int]] = Sink.fold(0)(_ + _)

  def sumSinkG[A](implicit m: Monoid[A]): Sink[A, Future[A]] = Sink.fold(m.empty)(m.combine)
}

object hellostream {

  import reusable._

  // TODO: use which actorsystem?
  implicit val system = ActorSystem("stream")
  implicit val ec = system.dispatcher
  val akkaTag = HashTag("#akka")
  val tweets: Source[Tweet, NotUsed] = Source(
    Tweet(Author("rolandkuhn"), System.currentTimeMillis, "#akka rocks!") ::
      Tweet(Author("patriknw"), System.currentTimeMillis, "#akka !") ::
      Tweet(Author("bantonsson"), System.currentTimeMillis, "#akka !") ::
      Tweet(Author("drewhk"), System.currentTimeMillis, "#akka !") ::
      Tweet(Author("ktosopl"), System.currentTimeMillis, "#akka on the rocks!") ::
      Tweet(Author("mmartynas"), System.currentTimeMillis, "wow #akka !") ::
      Tweet(Author("akkateam"), System.currentTimeMillis, "#akka rocks!") ::
      Tweet(Author("bananaman"), System.currentTimeMillis, "#bananas rock!") ::
      Tweet(Author("appleman"), System.currentTimeMillis, "#apples rock!") ::
      Tweet(Author("drama"), System.currentTimeMillis, "we compared #apples to #oranges!") ::
      Nil
  )

  def tweet(): Future[Any] = {
    val authors: Source[Author, NotUsed] =
      tweets.filter(_.hashtags.contains(akkaTag))
        .map(_.author)
    // runWith() is a convenience method that automatically ignores the materialized value of any other operators
    // except those appended by the runWith() itself. In the above example it translates to using Keep.right as the
    // combiner for materialized values.
    authors.runWith(Sink.foreach(println))
    authors.runForeach(println)
    // todo fixit
    // authors.runWith(Sink.foreach(println))(system)
    def hashtags: Source[HashTag, NotUsed] = tweets.mapConcat(_.hashtags.toList)
    println(hashtags)
    def writeAuthors: Sink[Author, NotUsed] = ???
    def writeHashTags: Sink[HashTag, NotUsed] = ???
    // Both Graph and RunnableGraph are immutable, thread-safe, and freely shareable.
    val g = RunnableGraph.fromGraph(GraphDSL.create() { implicit b =>
      import GraphDSL.Implicits._
      val bcast = b.add(Broadcast[Tweet](2))
      tweets ~> bcast.in // edge operator, connect, via, to
      bcast.out(0) ~> Flow[Tweet].map(_.author) ~> writeAuthors
      bcast.out(1) ~> Flow[Tweet].mapConcat(_.hashtags.toList) ~> writeHashTags
      // A Graph[ClosedShape, NotUsed] where ClosedShape means that it is a fully connected graph or “closed” - there
      // are no unconnected inputs or outputs
      ClosedShape
    })
    // A graph can also have one of several other shapes, with one or more unconnected ports. Having unconnected
    // ports expresses a graph that is a partial graph
    g.run()
    def slowComputation: Flow[Tweet, Int, NotUsed] = Flow[Tweet].map(_.hashtags.size)
    tweets.buffer(1000, OverflowStrategy.dropHead)
      .via(slowComputation)
      .runWith(Sink.ignore)
    // val counterGraph: Future[Int] = tweets.via(count).runWith(sumSink)
    // A RunnableGraph may be reused and materialized multiple times, because it is only the “blueprint” of the stream
    val counterGraph: RunnableGraph[Future[Int]] = tweets.via(count).toMat(sumSink)(Keep.right)
    val sum: Future[Int] = counterGraph.run()
    sum.foreach(c => println(s"Total tweets processed: $c"))
    ???
  }

  def s1(): Seq[Future[AnyRef]] = {
    val source = Source(1 to 10)
    val done: Future[Done] = source.runForeach(println)
    val factorials = source.scan(BigInt(1))(_ * _)
    val done2 = factorials
      .throttle(1, 1 second)
      .runForeach(x => println(s"fac: ${x}"))
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
    Future.sequence(s1() :+ tweet() :+ twitterTags(twitterTagsUrl))
      .onComplete(_ => system.terminate())
  }
}
