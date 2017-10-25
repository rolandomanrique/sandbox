import sbt._

scalaVersion in Global := "2.11.11"

libraryDependencies ++= Seq(
  "org.scalaz.stream" %% "scalaz-stream" % "0.7.3a",
  "co.fs2" %% "fs2-core" % "0.10.0-M6",
  "co.fs2" %% "fs2-io" % "0.10.0-M6",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "net.liftweb" %% "lift-json" % "2.6.+"
) 
