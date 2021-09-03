package net.zhenglai.services

trait UserComponent { this: DatabaseDaoComponent =>
  val userService: UserService

  class UserService {}

}
