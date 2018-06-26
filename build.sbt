name := "scalyrifi"

version := "0.1"

scalaVersion := "2.12.6"

libraryDependencies += "net.sourceforge.tess4j" % "tess4j" % "3.4.9"
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.13",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.13" % Test
)
