val AkkaVersion = "2.6.8"
val AkkaHttpVersion = "10.2.6"

ThisBuild / organization := "net.zhenglaizhang"
ThisBuild / version := "0.0.1-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.6"
//ThisBuild / idePackagePrefix := Some("net.zhenglai")

lazy val commonScalacOptions = Seq(
  "-unchecked",
  "-deprecation",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-language:postfixOps",
  "-Ywarn-dead-code",
  "-Xfatal-warnings",
  "-Xlint"
)

lazy val commonSettings = Seq(
  scalacOptions := commonScalacOptions,
  idePackagePrefix := Some("net.zhenglai")
)

lazy val root = (project in file("."))
  .aggregate(util, core)
  .settings(
    name := "github-scala"
  )

lazy val util = (project in file("util"))
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-core" % "2.6.1",
      "org.json4s" %% "json4s-native" % "4.0.3",
      "org.scalatest" %% "scalatest" % "3.0.8" % Test
    )
  )
lazy val core = (project in file("core"))
  .dependsOn(util % "compile->compile;test->test")
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
      "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
      "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion,
      "org.scalatest" %% "scalatest" % "3.0.8" % Test
    )
  )

lazy val hello = taskKey[Unit]("An example task")
