package net.zhenglai.github

import api.RepoAPI
import context.Implicits.GitHubContext

case class GitHubRepo(
    id: Long,
    name: String,
    full_name: String, // TODO: snake case (underscore notation)
    owner: Owner
)

object RepoAPI {
  def apply(implicit ctx: GitHubContext) = new RepoAPI()
}

class GitHub {
  def repos(implicit ctx: GitHubContext) = new RepoAPI()
}

object GitHub {
  def apply(token: String): GitHub = new GitHub
}

case class Owner(login: String, id: Long)
