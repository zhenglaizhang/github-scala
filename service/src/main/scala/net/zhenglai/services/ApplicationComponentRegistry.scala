package net.zhenglai.services

object ApplicationComponentRegistry
    extends UserComponent
    with DatabaseDaoComponent
    with DatabaseComponent
    with MigrationComponent {
  override val userService: ApplicationComponentRegistry.UserService =
    new UserService()
  override val dao: ApplicationComponentRegistry.Dao = new Dao()
  override val databaseService: DatabaseService =
    new H2DatabaseService("", "", "")
  override val migrationService: ApplicationComponentRegistry.MigrationService =
    new MigrationService()
}
