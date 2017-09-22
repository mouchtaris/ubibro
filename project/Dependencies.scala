import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.1"

  object scala {
    val version = "2.12.3"
  }

}
