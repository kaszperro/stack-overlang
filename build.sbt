name := "stack-overlang"

version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies += "org.scalaj" %% "scalaj-http" % "2.4.1"

val circeVersion = "0.10.0"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)