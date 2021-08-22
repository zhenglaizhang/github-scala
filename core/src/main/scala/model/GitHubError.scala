package net.zhenglai.github
package model

import akka.http.scaladsl.model.{StatusCode, StatusCodes}

abstract class GitHubError(msg: String) extends RuntimeException(msg) {
  def statusCode: StatusCode
}

final case class NotFoundError(
    s: String,
    statusCode: StatusCode = StatusCodes.NotFound
) extends GitHubError(s"$s not found")

final case class InvalidRequestError(
    s: String,
    statusCode: StatusCode = StatusCodes.BadRequest
) extends GitHubError(s)
