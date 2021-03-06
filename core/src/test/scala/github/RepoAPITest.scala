package net.zhenglai.github

import net.zhenglai.github.context.Implicits.Global
import org.scalatest.FunSuite

import scala.concurrent.Await
import scala.concurrent.duration._

class RepoAPITest extends FunSuite {
  val gitHubAPI = GitHub("token")

  // TODO: [info]   java.lang.IllegalArgumentException: eitherUnmarshaller only works with strict entities, so make
  //  sure to wrap routes that want to use it with `toStrictEntity`
  test("testListRepos") {
//    val f = gitHubAPI.repos.listRepos("wearexteam")
//    val r: GitHubResp[List[GitHubRepo]] = Await.result(f, 10.second)
//    println(r)
  }

  test("get returns good result") {
//    val owner = "wearexteam"
//    val repo = "spire"
//    val f = gitHubAPI.repos.get(owner, repo)
//    val r = Await.result(f, 10.seconds)
//    println(r)
//    assert(r.owner.login == owner)
//    assert(r.name == repo)
  }

  test("get returns not found") {
    val f = gitHubAPI.repos.get("wearexteam", "notexistrepo")
    val r = Await.result(f, 10.seconds)
    println(r)
  }

}
