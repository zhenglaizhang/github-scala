package net.zhenglai
package akkaz.streamm

import akka.actor.{ActorSystem, Cancellable}
import akka.stream.ClosedShape
import akka.stream.scaladsl.{Flow, GraphDSL, Keep, RunnableGraph, Sink, Source}

import scala.concurrent.{Future, Promise}

object AsyncB {
  implicit val system = ActorSystem("AsyncB")
  implicit val ec = system.dispatcher
  Source(List(1, 2, 3)).map(_ + 1).async.map(_ * 2).to(Sink.ignore)
  // A source that can be signalled explicitly from the outside
  val source: Source[Int, Promise[Option[Int]]] = Source.maybe[Int]

  // a Cancellable
  // which can be used to shut down the stream
  def flow: Flow[Int, Int, Cancellable] = ???

  // A sink that returns the first element of a stream in the returned Future
  val sink: Sink[Int, Future[Int]] = Sink.head[Int]
  val r2: RunnableGraph[Cancellable] = source.viaMat(flow)(Keep.right).to(sink)
  val r3: RunnableGraph[Future[Int]] = source.via(flow).toMat(sink)(Keep.right)
  // Using runWith will always give the materialized values of the stages added
  // by runWith() itself
  val r4: Future[Int] = source.via(flow).runWith(sink)
  val r5: Promise[Option[Int]] = flow.to(sink).runWith(source)
  val r6: (Promise[Option[Int]], Future[Int]) = flow.runWith(source, sink)
  val r7: RunnableGraph[(Promise[Option[Int]], Cancellable)] =
    source.viaMat(flow)(Keep.both).to(sink)
  val r9: RunnableGraph[((Promise[Option[Int]], Cancellable), Future[Int])] =
    source.viaMat(flow)(Keep.both).toMat(sink)(Keep.both)
  (Keep.both)

  val r11: RunnableGraph[(Promise[Option[Int]], Cancellable, Future[Int])] =
    r9.mapMaterializedValue {
      case ((promise, cancellable), future) =>
        (promise, cancellable, future)
    }

  // The result of r11 can be also achieved by using the Graph API
  val r12: RunnableGraph[(Promise[Option[Int]], Cancellable, Future[Int])] =
    RunnableGraph.fromGraph(GraphDSL.create(source, flow, sink)((_, _, _)) { implicit b => (src, f, dst) =>
      import GraphDSL.Implicits._
      src ~> f ~> dst
      ClosedShape
    })

  // Since every operator in Akka Streams can provide a materialized value after being materialized,
  def main(args: Array[String]): Unit = {
    val (promise, cancellable, future) = r11.run()
    promise.success(None)
    cancellable.cancel()
    val _ = future.map(_ + 3)
  }
}