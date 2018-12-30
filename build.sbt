
name := "scala-ts"

version := "0.1"

scalaVersion := "2.12.8"

// build.sbt
libraryDependencies += "org.scalameta" %% "scalameta" % "4.1.0"
libraryDependencies += "org.scalameta" %% "semanticdb" % "4.1.0"

scalacOptions += "-Yrangepos"

addCompilerPlugin("org.scalameta" % "semanticdb-scalac" % "4.1.0" cross CrossVersion.full)


