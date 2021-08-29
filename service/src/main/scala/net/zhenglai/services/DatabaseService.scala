package net.zhenglai.services

import java.sql.Connection

trait DatabaseService {
  val dbDriver: String
  val connectionString: String
  val username: String
  val password: String
  val ds = { /**/ }
  def getConnection: Connection = ???
}
