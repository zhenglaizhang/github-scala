package net.zhenglai
package alg

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpRequest
import akka.stream._
import akka.stream.scaladsl.{FileIO, Framing, Keep, RunnableGraph}
import akka.stream.stage.{GraphStageLogic, GraphStageWithMaterializedValue, InHandler}
import akka.util.ByteString
import cats.Show
import cats.implicits._
import java.io.Closeable
import java.nio.file.Paths
import scala.collection.mutable
import scala.concurrent.duration._
import scala.concurrent.{Await, Future, Promise}
import scala.io.Source
import scala.util.{Try, Using}


final class TopKStage[T](maxSize: Int)(implicit ord: Ordering[T])
  extends GraphStageWithMaterializedValue[SinkShape[T], Future[Seq[T]]] {
  val in = Inlet[T]("seq.in")

  override def toString: String = "SeqStage"

  override val shape: SinkShape[T] = SinkShape.of(in)

  // override protected def initialAttributes: Attributes = DefaultAttributes.seqSink

  override def createLogicAndMaterializedValue(inheritedAttributes: Attributes) = {
    val p: Promise[Seq[T]] = Promise()
    val logic = new GraphStageLogic(shape) with InHandler {
      val q = new BoundedMinHeap[T](maxSize)

      override def preStart(): Unit = pull(in)

      def onPush(): Unit = {
        q.add(grab(in))
        pull(in)
      }

      override def onUpstreamFinish(): Unit = {
        val result = q.result()
        p.trySuccess(result)
        completeStage()
      }

      override def onUpstreamFailure(ex: Throwable): Unit = {
        p.tryFailure(ex)
        failStage(ex)
      }

      override def postStop(): Unit = {
        if (!p.isCompleted) p.failure(new AbruptStageTerminationException(this))
      }

      setHandler(in, this)
    }
    (logic, p.future)
  }
}

  class TopKSink(maxSize: Int) extends GraphStageWithMaterializedValue[SinkShape[Movie], Future[Seq[Movie]]] {
    val in: Inlet[Movie] = Inlet("TopKSink")

    override def shape: SinkShape[Movie] = SinkShape(in)

    override def createLogicAndMaterializedValue(inheritedAttributes: Attributes): (GraphStageLogic, Future[Seq[Movie]])
    = {
      val promise = Promise[Seq[Movie]]()
      implicit val movieOrdering: Ordering[Movie] = Ordering.by(-_.rating)
      val logic: GraphStageLogic = new GraphStageLogic(shape) {
        val bpq = new BoundedMinHeap[Movie](maxSize)

        override def preStart(): Unit = pull(in)

        setHandler(in, new InHandler {
          override def onPush(): Unit = {
            val elem = grab(in)
            bpq.add(elem)
            pull(in)
          }

          override def onUpstreamFinish(): Unit = {
            promise.success(bpq.result())
          }
        })
      }
      (logic, promise.future)
    }
  }

  class BoundedMinHeap[A: Ordering](maxSize: Int) {
    private[this] val pq = mutable.PriorityQueue.empty[A]

    def add(x: A): Unit = {
      if (pq.size < maxSize) {
        pq.addOne(x)
      } else {
        if (implicitly[Ordering[A]].lt(x, pq.head)) {
          pq.dequeue()
          pq.addOne(x)
        }
      }
    }

    def result(): Seq[A] = pq.toSeq
  }

  object TopKMovie {
    final case class Movie(id: String, rating: Double)

    object Movie {
      def parse(s: String): Option[Movie] = {
        val pat = """^([\w"]+),(\d+)$""".r
        s match {
          case pat(id, rs) => rs.toDoubleOption.map(Movie(id, _))
          case _ => None
        }
      }
    }

    object using {
      def apply[A <: Closeable, B](res: => A)(f: A => B): Option[B] = {
        var resOpt: Option[A] = None
        try {
          resOpt = Try(res).toOption
          resOpt.map(f)
        } finally {
          for {
            r <- resOpt
          } r.close()
        }
      }
    }

    def testStream(): Unit = {
      implicit val system = ActorSystem("testStream")
      implicit val ec = system.dispatcher
      implicit val movieRatingOrdering: Ordering[Movie] = Ordering.by(-_.rating)
      val g: GraphStageWithMaterializedValue[SinkShape[Movie], Future[Seq[Movie]]] = {
        new TopKStage[Movie](4)
      }
      val r: scaladsl.Source[Movie, Future[IOResult]] =
        FileIO.fromPath(Paths.get("play/scala2/src/main/scala/alg/movies.csv"))
          .via(Framing.delimiter(delimiter = ByteString("\n"), maximumFrameLength = 512, allowTruncation = true)
            .drop(1)
            .map(_.utf8String)
            .mapConcat(s => Movie.parse(s).toSeq))
      val res: RunnableGraph[Future[Seq[Movie]]] = r.toMat(g)(Keep.right)
      res.run().foreach(println)
    }

    def testBPQ(): Seq[Movie] = {
      implicit val movieOrdering = new Ordering[Movie] {
        override def compare(x: Movie, y: Movie): Int = {
          if (x.rating > y.rating) -1
          else if (x.rating == y.rating) 0
          else 1
        }
      }
      val bpq = new BoundedMinHeap[Movie](4)
      Using(Source.fromFile(("play/scala2/src/main/scala/alg/movies.csv"))) { s =>
        s.getLines()
          .flatMap(Movie.parse)
          .foreach { m =>
            bpq.add(m)
          }
      }
      bpq.result()
    }

    def testHttpClient2(uri: String): Unit = {
      implicit val system = ActorSystem("Test2")
      implicit val ec = system.dispatcher
      Http().singleRequest(HttpRequest(uri = uri))
        .flatMap { r =>
          implicit val ordering: Ordering[Movie] = Ordering.by(x => x.rating)
          val bpq = new BoundedMinHeap[Movie](4)(ordering.reverse)
          r.entity
            .dataBytes
            .via(Framing.delimiter(ByteString("\n"), maximumFrameLength = 1000, allowTruncation = true))
            .mapConcat(bs => Movie.parse(bs.utf8String).toSeq)
            .runForeach(bpq.add)
            .map(_ => bpq.result())
        }
        .foreach(println)
    }

    val uri = "https://gist.githubusercontent" +
      ".com/zhenglaizhang/e44992fbe1a6cdeeeed76186ea39aee5/raw/28ac065ffc7e328f0acda327999a642f5232be48/movies.csv"

    def testHttpClient1(): Unit = {
      implicit val system = ActorSystem("Interview")
      implicit val ec = system.dispatcher
      val rf = Http().singleRequest(HttpRequest(uri = uri))
        .flatMap(_.entity.dataBytes.runFold(ByteString(""))(_ ++ _))
        .map(_.utf8String)
        .map { s =>
          s.split("\n")
            .flatMap(Movie.parse)
            .toVector
            .sortWith(_.rating > _.rating)
            .take(4)
        }
      val r = Await.result(rf, 4 seconds)
      println(r)
    }

    def testSimple(): Unit = {
      val top4 = using(Source.fromFile("play/scala2/src/main/scala/alg/movies.csv")) { s =>
        s.getLines()
          .flatMap(Movie.parse)
          .toVector
          .sortWith(_.rating > _.rating)
          .take(4)
      }
      println(top4)
    }

    def main(args: Array[String]): Unit = {
//      val r = testBPQ()
//      println(r)
//      testSimple()
//      testHttpClient1()
//      testHttpClient2()
      testStream()
//      testHttpClient2("http://localhost:3000/download")
    }
  }

  object ColTest {
    implicit class MyArrayOps[A](xs: Array[A]) {
      def asString: String = xs.mkString("[", ",", "]")
    }

    implicit def arrayShow[A]: Show[Array[A]] = Show.show(_.mkString("[", ",", "]"))

    def main(args: Array[String]): Unit = {
      val x: Array[Int] = Array(1, 2, 3, 4)
      println(x.asString)
      println(x.show)
      println(Array("abc", "dafda", "da").show)
      println(x.toVector)
    }
  }