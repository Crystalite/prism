name := "prism"

version := "1.0"

scalaVersion := "2.11.8"

organization := "xyz.crystalite"

resolvers += Resolver.bintrayRepo("cakesolutions", "maven")

libraryDependencies ++= {
  val akkaVersion = "2.4.4"
  val circeV = "0.4.1"

  Seq(
    "com.typesafe.akka" %% "akka-actor"      % akkaVersion,
    "com.typesafe.akka" %% "akka-http-core"  % akkaVersion,
    "de.heikoseeberger" %% "akka-http-circe" % "1.6.0",

    "io.circe" %% "circe-core" % circeV,
    "io.circe" %% "circe-generic" % circeV,
    "io.circe" %% "circe-parser" % circeV,

    "net.cakesolutions" %% "scala-kafka-client-akka" % "0.7.0"
  )
}