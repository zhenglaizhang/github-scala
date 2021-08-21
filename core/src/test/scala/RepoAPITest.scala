package net.zhenglai.github

import context.Implicits.Global

import org.scalatest.FunSuite

import scala.concurrent.Await
import scala.concurrent.duration._

class RepoAPITest extends FunSuite {
  val gitHubAPI = GitHub("token")

  test("testListRepos") {
    val f = gitHubAPI.repos.listRepos("wearexteam")
    val r = Await.result(f, 10.second)
    assert(r.head.name === "spire")
//    println(s"r = ${r}")
  }

  test("testget") {
    val f = gitHubAPI.repos.get("wearexteam", "spire")
    val r = Await.result(f, 10.seconds)
    println(r)
  }

}
