val dottyVersion = "0.4.0-RC1"

lazy val root = (project in file(".")).
  settings(
    name := "dotty-simple",
    version := "0.1",

    scalaVersion := dottyVersion,

    libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test",

    scalacOptions ++= Seq(
      "-explain",
      "-explain-implicits",
      "-explain-types",
      "-feature",
      "-uniqid",
      "-Xfatal-warnings",
      "-Yno-imports"
    )
  )
