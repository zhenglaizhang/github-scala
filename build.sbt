import sbt.Keys.libraryDependencies

val AkkaVersion = "2.6.8"
val AkkaHttpVersion = "10.2.6"

ThisBuild / organization := "net.zhenglaizhang"
ThisBuild / version := "0.0.1-SNAPSHOT"
//ThisBuild / scalaVersion := "2.13.6"
lazy val scala2Version = "2.13.6"
lazy val scala3Version = "3.0.1"

def commonScalacOpts(scalaVersion: String) =
  Seq(
    //    "-encoding",
    //    "UTF-8",
    "-unchecked",
    "-deprecation",
    "-feature",
    "-language:existentials",
    "-language:higherKinds",
    "-language:implicitConversions",
    "-language:postfixOps",
    "-Xfatal-warnings"
  ) ++ (CrossVersion.partialVersion(scalaVersion) match {
    case Some((3, _)) =>
      Seq(
        // "-unchecked"
        "-Yindent-colons",
        "-source:future",
        "-Ysafe-init"
      )
    case _ =>
      Seq(
        "-Ymacro-annotations",
        "-deprecation",
        "-Ywarn-dead-code",
        "-Xlint",
        "-Xfatal-warnings",
        "-Wunused:imports,privates,locals",
        "-Wvalue-discard"
      )
  })

def commonSettings(scalaV: String) =
  Seq(
    scalaVersion := scalaV,
    scalacOptions := commonScalacOpts(scalaV),
    idePackagePrefix := Some("net.zhenglai")
  )

lazy val root = (project in file("."))
  .aggregate(util, core, service, rtbCore, play, scala3Play, playZio)
  .settings(
    name := "github-scala"
  )

lazy val util = (project in file("util"))
  .settings(
    commonSettings(scala2Version),
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
    commonSettings(scala2Version),
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
      "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
      "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion,
      "org.scalatest" %% "scalatest" % "3.0.8" % Test,
      "io.swagger.parser.v3" % "swagger-parser" % "2.0.27"
    )
  )

lazy val service = (project in file("service"))
  .dependsOn(core % "compile->compile;test->test")
  .settings(
    commonSettings(scala2Version),
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
    commonSettings(scala2Version)
  )

lazy val playZio = (project in file("play/zio"))
  .dependsOn(util % "compile->compile;test->test")
  .settings(
    commonSettings(scala2Version),
    resolvers += Resolver.sonatypeRepo("snapshots"),
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % "1.0.12",
      "dev.zio" %% "zio-streams" % "1.0.12"
    )
  )

lazy val play = (project in file("play/scala2"))
  .dependsOn(util % "compile->compile;test->test")
  .settings(
    commonSettings(scala2Version),
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-reflect" % scalaVersion.value,
      "io.estatico" %% "newtype" % "0.4.4",
      "eu.timepit" %% "refined" % "0.9.27",
      "org.typelevel" %% "cats-core" % "2.6.1",
      "org.json4s" %% "json4s-native" % "4.0.3",
      "org.scalatest" %% "scalatest" % "3.0.8" % Test,
      "com.fasterxml.jackson.core" % "jackson-databind" % "2.12.4",
      "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.12.4"
    )
  )

lazy val scala3Play = (project in file("scala3/play"))
  .dependsOn(util % "compile->compile;test->test")
  .settings(
    commonSettings(scala3Version),
    libraryDependencies ++= Seq(
      // "org.typelevel" %% "cats-core" % "2.6.1"
      // "dev.optics" %% "monocle-core"  % "3.0.0",
      // "org.scalatest" %% "scalatest" % "3.2.9" % Test
    )
  )

lazy val hello = taskKey[Unit]("An example task")
