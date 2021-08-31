package net.zhenglai.services

trait DatabaseDaoComponent { this: DatabaseComponent =>
  val dao: Dao

  class Dao() {}
}
