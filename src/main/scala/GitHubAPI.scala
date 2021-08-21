package net.zhenglai.github

import httpclient.AkkaHttpClient

import scala.concurrent.Future

case class GitHubRepo(
    id: Long,
    name: String,
    full_name: String, // TODO: snake case (underscore notation)
    owner: Owner
)

class RepoAPI {
  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
  import spray.json.DefaultJsonProtocol._

  import scala.concurrent.ExecutionContext.Implicits.global
  implicit val ownerFormat = jsonFormat2(Owner)
  implicit val gitHubRepoFormat = jsonFormat4(GitHubRepo)
  val http = new AkkaHttpClient()

  // TODO: paging
  def listRepos(org: String): Future[List[GitHubRepo]] = {
    http.newRequest[List[GitHubRepo]](
      s"https://api.github.com/orgs/$org/repos"
    )
  }

}

class GitHubAPI {
  val repos = new RepoAPI
}

object GitHubAPI {
  def apply(token: String): GitHubAPI = new GitHubAPI
}

case class Owner(login: String, id: Long)
