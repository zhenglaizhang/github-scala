package net.zhenglai
package basic

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import scala.concurrent.{Future, Promise}

// Synchronous, blocking
// Our job is to get the most out of our CPU, but some function calls invoke some sort of resource (like a database),
// or wait for it to start, or wait for a response. That is called a blocking call. Blocking, because when you call
// the function, you can’t do anything until you get a result
object blocking {
  def blockingFunction(n: Int): Int = {
    Thread.sleep(1000)
    n + 42
  }

  def main(args: Array[String]): Unit = {
    println("start right now")
    // The main downside of blocking calls is that the calling thread is neither doing any work, nor is it making any
    // progress, nor is it yielding control to something else.
    blockingFunction(3)
    println("I have to wait for 1000 millis")
  }
}

// Asynchronous, blocking
// In Scala, an asynchronous computation is called a Future, and it can be evaluated on another thread
object async_blocking {

  import scala.concurrent.Future
  import scala.concurrent.ExecutionContext.Implicits.global

  def asyncBlockingFunction(n: Int): Future[Int] = Future {
    Thread.sleep(1000)
    n + 42
  }

  def main(args: Array[String]): Unit = {
    asyncBlockingFunction(3)
    val v = 32
    println(s"I show immediately with ${v}")
  }
  // The value after the call evaluates immediately, because the actual computation of the async method will run in
  // parallel, on another thread. This is asynchronous. However, it’s also blocking because, although you’re not
  // blocking the main flow of the program, you’re still blocking some thread at (or close to) the moment you’re
  // calling the method. It’s like passing the burning coal from your hand to someone else.
  //
  // The blocking aspect comes from the fact that these kinds of computations need to be constantly monitored for
  // completion. It’s like you spawn an annoying parrot, saying:
  //
  //“Are you done?” “Are you done?” “Are you done?” “Are you done?” “How about now?”
  //
  // so that when you are indeed done, the parrot will say “roger that” and will fly back to you (the calling thread)
  // to deliver the result.
}

// Asynchronous, non-blocking
// The true non-blocking power comes from actions that do not block either you (the calling thread) or someone else
// (some secondary thread). Best exemplified with an Akka actor. An actor, unlike what you may have read from the
// webs, is not something active. It’s just a data structure. The power of Akka comes from the fact that you can
// create a huge amount of actors (millions per GB of heap), so that a small number of threads can operate on them in
// a smart way, via scheduling
object async_nonblocking {
  def createSimpleActor(): Behaviors.Receive[String] = Behaviors.receiveMessage[String] { msg =>
    println(s"Received msg: $msg")
    Behaviors.same
  }

  val promiseResolver: ActorSystem[(String, Promise[Int])] = ActorSystem(
    Behaviors.receiveMessage[(String, Promise[Int])] {
      case (msg, promise) =>
        promise.success(msg.length)
        Behaviors.same
    },
    "promiseResolver"
  )

  // This actor will complete a promise when it receives a message. On the other end - in the calling thread - we
  // could process this promise when it’s complete. Let’s define some sensible API that would wrap this asynchronous,
  // non-blocking interaction:
  def doAsyncNonBlockingThing(s: String): Future[Int] = {
    val promise: Promise[Int] = Promise[Int]()
    promiseResolver ! ((s, promise))
    promise.future
  }

  def main(args: Array[String]): Unit = {
    val rootActor = ActorSystem(createSimpleActor(), "TestSystem")
    // Then calling the tell method (!) on the actor is completely asynchronous and non-blocking. Why non-blocking?
    // Because this doesn’t block the calling thread - the tell method returns immediately - and also because it
    // doesn’t spawn (or block) any other thread. Because Akka has an internal thread scheduler, it will be some
    // point in the future when a thread will be scheduled to dequeue this message out of the actor’s mailbox and
    // process it for me.
    rootActor ! "Hello"
    val asyncNonblockingResult = doAsyncNonBlockingThing("Hello")
    import scala.concurrent.ExecutionContext.Implicits.global
    asyncNonblockingResult.onComplete(v => println(s"I have got a non-blocking async answer: $v"))
    // In this way, neither the calling thread, nor some other thread is immediately used by the call, and we still
    // return meaningful values from the interaction, which we can register a callback on when complete. For some
    // reason, the ask pattern in Akka Typed is very convoluted, but on that, another time.
  }
}