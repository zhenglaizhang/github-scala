package net.zhenglai.github

import org.scalatest.FunSuite

import scala.concurrent.Await
import scala.concurrent.duration._
import net.zhenglai.github.context.Implicits.Global

class RepoAPITest extends FunSuite {

  test("testListRepos") {
    val gitHubAPI = GitHubAPI("token")
    val f = gitHubAPI.repos.listRepos("wearexteam")
    val r = Await.result(f, 10.second)
    println(s"r = ${r}")
  }

}
