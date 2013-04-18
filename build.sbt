name := "dispatch-lift-json"

organization := "net.databinder"

version := "0.8.9"

scalaVersion := "2.10.1"

WebDav.globalSettings

libraryDependencies ++= Seq(
  "net.databinder" %% "dispatch-core" % "0.8.9",
  "org.json4s" %% "json4s-native" % "3.2.4",
  "net.databinder" %% "dispatch-http" % "0.8.9" % "test",
  "org.scala-tools.testing" %% "specs" % "1.6.9" % "test"
)
