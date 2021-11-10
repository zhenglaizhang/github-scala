package net.zhenglai
package akkaz.streamm

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.scaladsl.{Flow, Keep, RunnableGraph, Sink, Source}
import scala.concurrent.Future

// By default, Akka Streams elements support exactly one downstream operator.
// - broadcast (signals all down-stream elements)
// - balance (signals one of available down-stream elements).
// A stream can be materialized multiple times, the materialized value will also be calculated anew for each such
// materialization

object Sourcew {
  implicit val system = ActorSystem("Sourcew")
  implicit val ec = system.dispatcher
  val source = Source(1 to 10)
  // FoldSink materializes a value of type Future which will represent the result of the folding process over the
  // stream
  val sink = Sink.fold[Int, Int](0)(_ + _)
  // connect the Source to the Sink, obtaining a RunnableGraph
  // After running (materializing) the RunnableGraph[T] we get back the materialized value of type T. Every stream
  // operator can produce a materialized value, and it is the responsibility of the user to combine them to a new
  // type. In the above example, we used toMat to indicate that we want to transform the materialized value of the
  // source and sink, and we used the convenience function Keep.right to say that we are only interested in the
  // materialized value of the sink.
  val runnable: RunnableGraph[Future[Int]] = source.toMat(sink)(Keep.right)
  // materialize the flow and get the value of the FoldSink
  val r: Future[Int] = runnable.run()
  // materialize the flow, getting the Sinks materialized value
  val sum: Future[Int] = source.runWith(sink)

  // operators are immutable, connecting them returns a new operator, instead of modifying the existing instance
  def main(args: Array[String]): Unit = {
    r.foreach(println)
    sum.foreach(println)
  }

  def wireup(): Unit = {
    Source(1 to 5).via(Flow[Int].map(_ * 2)).to(Sink.foreach(println))
    val sink: Sink[Int, NotUsed] = Flow[Int].map(_ * 2).to(Sink.foreach(println))
    Source(1 to 6).to(sink)
    val otherSink: Sink[Int, NotUsed] = Flow[Int].alsoTo(Sink.foreach(println)).to(Sink.ignore)
    val _ = Source(1 to 6).to(otherSink)
  }

  Source(List(1, 2, 3))
  Source.future(Future.successful("hello streams"))
  Source.single("only one element")
  Source.empty
  Sink.fold[Int, Int](0)(_ + _)
  Sink.head
  Sink.ignore
  Sink.foreach[String](println(_))
}