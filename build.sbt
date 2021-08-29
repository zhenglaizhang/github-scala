val AkkaVersion = "2.6.8"
val AkkaHttpVersion = "10.2.6"

ThisBuild / organization := "net.zhenglaizhang"
ThisBuild / version := "0.0.1-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.6"

lazy val commonScalacOptions = Seq(
  "-unchecked",
  "-deprecation",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-language:postfixOps",
  "-Ywarn-dead-code",
//  "-Xfatal-warnings",
  "-Xlint",
  "-Ymacro-annotations" // only available for Scala 2.13
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
      "dev.optics" %% "monocle-core" % "3.0.0",
      "dev.optics" %% "monocle-macro" % "3.0.0", // only for Scala 2.13
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

lazy val service = (project in file("service"))
  .dependsOn(core % "compile->compile;test->test")
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
      "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
      "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion,
      "org.scalatest" %% "scalatest" % "3.0.8" % Test,
      "org.scalatestplus" %% "mockito-3-4" % "3.2.9.0" % Test
    )
  )

lazy val rtbCore = (project in file("rtb/core"))
  .dependsOn(util)
  .settings(
    commonSettings
  )

lazy val play = (project in file("play"))
  .dependsOn(util % "compile->compile;test->test")
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-core" % "2.6.1",
      "org.json4s" %% "json4s-native" % "4.0.3",
      "org.scalatest" %% "scalatest" % "3.0.8" % Test
    )
  )

lazy val hello = taskKey[Unit]("An example task")
