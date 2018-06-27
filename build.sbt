name := "scalyrifi"
version := "0.1"
scalaVersion := "2.12.6"
libraryDependencies += "net.sourceforge.tess4j" % "tess4j" % "3.4.9"
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.13",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.13" % Test)

libraryDependencies += "org.seleniumhq.selenium" % "selenium-java" % "2.8.0"
libraryDependencies += "com.machinepublishers" % "jbrowserdriver" % "0.17.11"
libraryDependencies += "org.jsoup" % "jsoup" % "1.11.3"




