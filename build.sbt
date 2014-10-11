name := "kinesis-om-consumer"

version := "0.1"

scalaVersion := "2.11.1"

resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play" % "2.3.0",
  "com.amazonaws" % "aws-java-sdk" % "1.6.11",
  "com.amazonaws" % "amazon-kinesis-client" % "1.1.1",
  "com.typesafe.play" % "play-ws_2.11" % "2.3.5"
)

lazy val root = (project in file(".")).enablePlugins(PlayScala)
