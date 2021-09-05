package net.zhenglai
package types.control

import scala.language.reflectiveCalls
import scala.util.control.NonFatal

object manage {
  def apply[R <: { def close(): Unit }, B](resource: => R)(
      using: R => B
  ): B = {
    var res: Option[R] = None
    try {
      res = Some(resource)
      using(res.get)
    } catch {
      case NonFatal(ex) =>
        println(s"manage.apply(): Non fatal exception: $ex")
        throw ex
    } finally {
      res.foreach(_.close())
    }
  }
}
