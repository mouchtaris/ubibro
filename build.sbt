import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "jm.ubi",
      scalaVersion := scala.version,
      version      := "0.1.0-SNAPSHOT"
    )),
    inThisBuild {
      import ScalacOptions._
      standard ++ inConsole ++ inDoc ++ inDocSettings
    },
    name := "Bro",
    libraryDependencies += scalaTest % Test,
    libraryDependencies += "org.scala-lang" % "scala-reflect" % scala.version
  )
//  .enablePlugins(ScalaNativePlugin)
