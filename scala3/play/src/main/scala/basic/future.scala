package net.zhenglai
package basic

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}


@main def nondeterministic(): Unit = {
  // Futures are inherently non-deterministic
  // the value inside will be evaluated on “some” thread, at “some” point in time, without your control.
  val f = Future { 42 }
}

object MyService {
  def produceThePreciousValue(arg: Int): String = "i am a precious value"
  def submitTask[A](arg: A)(f: A => Unit): Boolean = {
    // send the function to be evaluated on some thread, at the discretion of the scheduling logic
    true
  }
  // the service has two API methods:
  // 1) A "production" function which is completely deterministic.
  // 2) A submission function which has a pretty terrible API, because the function argument will be evaluated on one
  //    of the service's threads and you can't get the returned value back from another thread's call stack.
  // the “production” logic is completely fixed and deterministic.

}

case object notdeterministic {
  // However, what’s not deterministic is when the service will actually end up calling the production function. In
  // other words, you can’t implement your function as below
  // because spawning up the thread responsible for evaluating the production function is not up to you.
  def givemeMyPreciousValue(arg: Int): Future[String] = Future {
    MyService.produceThePreciousValue(arg)
  }
}

// Promises - a “controller” and “wrapper” over a Future.
// You create a Promise, get its Future and use it (consume it) with the assumption it will be filled in later
case object deterministic {
  val p = Promise[String]()
  val f = p.future
  val fp = f.map(_.toUpperCase)

  // Then pass that promise to someone else, perhaps an asynchronous service:
  def asyncCall(p: Promise[String]): Unit = {
    // And at the moment the promise contains a value, its future will automatically be fulfilled with that value, which will unlock the consumer.
    p.success("you value here")
  }

  def givemeMyPreciousValue(arg: Int): Future[String] = {
    val p = Promise[String]()
    // submit a task to be evaluated later, at the discretion of the service
    // note: if the service is not on the same JVM, you can pass a tuple with the arg and the promise so the service has access to both
    MyService.submitTask(arg) { (x: Int) =>
      val pv = MyService.produceThePreciousValue(x)
      p.success(pv)
    }
    // return the future now, so it can be reused by whoever's consuming it
    p.future
  }
}
// So we create a promise and then we return its future at the end, for whoever wants to consume it. In the middle,
// we submit a function which will be evaluated at some point, out of our control. At that moment, the service
// produces the value and fulfils the Promise, which will automatically fulfil the Future for the consumer.
//
// This is how we can leverage the power of Promises to create “controllable” Futures, which we can fulfil at a moment
// of our choosing. The Promise class also has other methods, such as failure, trySuccess/tryFailure and more.
