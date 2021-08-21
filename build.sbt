name := "github-scala"
version := "0.1"
scalaVersion := "2.13.6"
idePackagePrefix := Some("net.zhenglai.github")

val AkkaVersion = "2.6.8"
val AkkaHttpVersion = "10.2.6"
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
  "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion
)
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8" % Test
