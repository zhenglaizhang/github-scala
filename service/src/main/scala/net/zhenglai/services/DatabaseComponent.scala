package net.zhenglai.services

trait DatabaseComponent {
  val databaseService: DatabaseService

  class H2DatabaseService(
      override val connectionString: String,
      override val username: String,
      override val password: String
  ) extends DatabaseService {
    override val dbDriver: String = "org.h2.Driver"
  }
}
