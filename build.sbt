import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "jm.ubi",
      scalaVersion := "2.12.3",
      version      := "0.1.0-SNAPSHOT"
    )),
    inThisBuild {
      import ScalacOptions._
      standard ++ inConsole ++ inDoc ++ inDocSettings
    },
    name := "Bro",
    libraryDependencies += scalaTest % Test
  )
