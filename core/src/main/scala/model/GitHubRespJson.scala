package net.zhenglai
package model

import github.model.{GitHubError, GitHubErrorJsonFormat, GitHubResp}

import spray.json.{DefaultJsonProtocol, JsObject, JsValue, RootJsonFormat}

object GitHubRespJson extends DefaultJsonProtocol {
  implicit def gitHubRespJsonFormat[A](implicit
      jsonFormat: RootJsonFormat[A]
  ): RootJsonFormat[GitHubResp[A]] =
    new RootJsonFormat[GitHubResp[A]] {
      override def write(obj: GitHubResp[A]): JsValue =
        obj match {
//          case l @ Left(value: GitHubError) =>
          case l: Left[GitHubError, A] =>
            GitHubErrorJsonFormat.GitHubErrorJsonFormat.write(l.value)
          case r: Right[GitHubError, A] => jsonFormat.write(r.value)
        }
      override def read(json: JsValue): GitHubResp[A] = {
        json match {
          case obj: JsObject if obj.fields.contains("message") =>
            Left(GitHubErrorJsonFormat.GitHubErrorJsonFormat.read(obj))
          case obj: JsObject if !obj.fields.contains("message") =>
            Right(jsonFormat.read(obj))
          case _ =>
            throw new RuntimeException(s"unknown json: ${json.toString()}")
        }
      }
    }
}
