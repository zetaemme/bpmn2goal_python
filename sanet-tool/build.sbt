name := "sanet-tool"

version := "0.1"

scalaVersion := "2.12.8"

resolvers += "Sonatype OSS Snapshots" at
  "https://oss.sonatype.org/content/repositories/releases"

libraryDependencies += "com.storm-enroute" %% "scalameter" % "0.8.2"
libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.1.1"
libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.1"


testFrameworks += new TestFramework("org.scalameter.ScalaMeterFramework")

parallelExecution in Test := false
