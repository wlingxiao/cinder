organization := "com.github.wlingxiao"
name := "cinder"
version := "0.0.1-SNAPSHOT"
scalaVersion := "2.12.7"

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-language:implicitConversions",
  "-unchecked"
)

val BleakVersion = "0.0.2-SNAPSHOT"

libraryDependencies ++= Seq(
  "com.github.wlingxiao" %% "bleak-core" % BleakVersion,
  "com.github.wlingxiao" %% "bleak-netty" % BleakVersion,
  "com.github.wlingxiao" %% "bleak-swagger" % BleakVersion,
  "com.github.wlingxiao" %% "bleak-cli" % BleakVersion,

  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.9.4",
  "org.webjars" % "swagger-ui" % "3.20.2",
  "ch.qos.logback" % "logback-classic" % "1.2.3"
)

mainClass in assembly := Some("cinder.CinderApp")
assemblyMergeStrategy in assembly := {
  case PathList("META-INF", "io.netty.versions.properties") => MergeStrategy.first
  case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
  case _ => MergeStrategy.first
}