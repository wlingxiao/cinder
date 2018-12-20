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

libraryDependencies ++= Seq(
  "com.github.wlingxiao" %% "bleak-core" % "0.0.2-SNAPSHOT",
  "com.github.wlingxiao" %% "bleak-netty" % "0.0.2-SNAPSHOT",
  "com.github.wlingxiao" %% "bleak-swagger" % "0.0.2-SNAPSHOT",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.9.4",
)