name := "Hello"

version := "0.0.1"

scalaVersion := "2.11.7"

publishTo := Some(Resolver.file("hello",file("/repo"))(Patterns(true, Resolver.mavenStyleBasePattern)))

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-json" % "2.6.0"
)

libraryDependencies += "com.typesafe.play" %% "play-json-joda" % "2.6.0"