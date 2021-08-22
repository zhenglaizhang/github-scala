package net.zhenglai.github
package api

import scala.concurrent.Future

class UserAPI {
  // GET /users/${login}
  def get(login: String): Future[User] = ???
}

case class User(login: String, id: Long, url: String, `type`: String)
