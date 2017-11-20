name := "sakuraiowebhook"

version := "0.2.0"

scalaVersion := "2.11.7"

publishTo := Some(Resolver.file("sakuraiowebhook",file("docs"))(Patterns(true, Resolver.mavenStyleBasePattern)))

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-json" % "2.6.0"
)

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.4"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.4" % "test"
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test

libraryDependencies += "com.typesafe.play" %% "play-json-joda" % "2.6.0"

  


testOptions += Tests.Argument(TestFrameworks.ScalaTest, "-u", {
  val dir = System.getenv("CI_REPORTS");
  if (dir == null) "target/reports" else dir
})

testOptions in Test += Tests.Argument("-oI")