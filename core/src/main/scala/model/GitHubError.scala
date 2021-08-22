package net.zhenglai.github
package model

import spray.json.{
  DefaultJsonProtocol,
  JsObject,
  JsString,
  JsValue,
  RootJsonFormat,
  enrichAny
}

abstract class GitHubError(message: String) extends RuntimeException(message) {
//  def statusCode: StatusCode
}

final case class NotFoundError(
    message: String,
    documentation_url: String
) extends GitHubError(message)

final case class InvalidRequestError(
    message: String,
    documentation_url: String
) extends GitHubError(message)

object GitHubErrorJsonFormat extends DefaultJsonProtocol {
  implicit val notFoundErrorFormat: RootJsonFormat[NotFoundError] = jsonFormat2(
    NotFoundError
  )
  implicit val invalidRequestErrorFormat: RootJsonFormat[InvalidRequestError] =
    jsonFormat2(InvalidRequestError)

  implicit object GitHubErrorJsonFormat extends RootJsonFormat[GitHubError] {
    override def write(ge: GitHubError): JsValue =
      ge match {
        case ire: InvalidRequestError => ire.toJson
        case nfe: NotFoundError       => nfe.toJson
        case _                        => throw new RuntimeException("unknown github error")
      }

    override def read(json: JsValue): GitHubError =
      json match {
        case obj: JsObject
            if (obj.fields("message") == JsString("Not Found")) =>
          obj.convertTo[NotFoundError]
        case obj: JsObject
            if (obj.fields("message") == JsString("Bad Request")) =>
          obj.convertTo[InvalidRequestError]
        // TODO: understand why?
//        case obj: JsObject =>
//          println(s"[${obj.fields("message").toString()}]")
//          println(obj.fields("message").toString() == "Not Found")
//          ???
        case _ => throw new RuntimeException("unknown github error")
      }
  }
}
