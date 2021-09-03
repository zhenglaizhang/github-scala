package net.zhenglai.services

trait MigrationComponent { this: DatabaseComponent =>
  val migrationService: MigrationService

  class MigrationService() {
    def runMigrations(): Unit = {
      val c = databaseService.getConnection
      println(c)
      println("start some migrations")
    }
  }
}
