import scala.annotation.threadUnsafe

case class DbConnection() {
  println("In constructor")
  type MySQLConnection = String

  // use faster mechanism which is not thread safe
  @threadUnsafe
  lazy val connection: MySQLConnection = {
    println("connecting")
    "DB"
  }
}

val db = DbConnection()
db.connection
db.connection
