name := "dispatch-lift-json"

organization := "net.databinder"

version := "0.8.8"

scalaVersion := "2.9.1"

libraryDependencies ++= Seq(
  "net.databinder" %% "dispatch-core" % "0.8.8",
  "net.liftweb" %% "lift-json" % "2.4",
  "net.databinder" %% "dispatch-http" % "0.8.8" % "test",
  "org.scala-tools.testing" %% "specs" % "1.6.9" % "test"
)
