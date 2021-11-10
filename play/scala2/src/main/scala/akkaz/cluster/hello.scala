//package net.zhenglai
//package akkaz.cluster
//
//import akka.actor.typed._
//import akka.actor.typed.scaladsl._
//import akka.actor.{Address, AddressFromURIString}
//import akka.cluster.ClusterEvent._
//import akka.cluster.typed._
//import scala.concurrent.ExecutionContextExecutor
//
//object HelloCluster {
//  implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.ignore, "HelloCluster")
//  implicit val ec: ExecutionContextExecutor = system.executionContext
//  val cluster: Cluster = Cluster(system)
//
//  def oops(): Unit = {
//    val selfMember = Cluster(system).selfMember
//    if (selfMember.hasRole("backend")) {
//      // context.spawn(Backend(), "back")
//    } else if (selfMember.hasRole("frontend")) {
//      // context.spawn(Frontend(), "front")
//    }
//
//    cluster.manager ! Join(cluster.selfMember.address)
//    cluster.manager ! Leave(cluster.selfMember.address)
//    cluster.manager ! Down(cluster.selfMember.address)
//
//    def subscriber = ???
//
//    cluster.subscriptions ! Subscribe(subscriber, classOf[MemberEvent])
//    //subscriber will receive events MemberLeft, MemberExited and MemberRemoved...
//    val _: CurrentClusterState = cluster.state
//    // this state is not necessarily in sync with the events published to a cluster subscription
//    val seedNodes: List[Address] =
//      List("akka://ClusterSystem@127.0.0.1:2551",
//        "akka://ClusterSystem@127.0.0.1:2551")
//        .map(AddressFromURIString.parse)
//    Cluster(system).manager ! JoinSeedNodes(seedNodes)
//  }
//
//  def main(args: Array[String]): Unit = {
//  }
//}