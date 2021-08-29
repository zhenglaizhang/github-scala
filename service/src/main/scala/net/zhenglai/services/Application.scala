package net.zhenglai.services

object Application {
  import ApplicationComponentRegistry._

  def main(args: Array[String]): Unit = {
    migrationService.runMigrations()
  }

}
