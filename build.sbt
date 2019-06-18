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

val scursesVersion = "1.0.1"

libraryDependencies += "net.team2xh" %% "onions" % scursesVersion
libraryDependencies += "net.team2xh" %% "scurses" % scursesVersion

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"