name := "prism"

version := "1.0"

scalaVersion := "2.11.8"

organization := "xyz.crystalite"

resolvers += Resolver.bintrayRepo("cakesolutions", "maven")

libraryDependencies ++= {
  val akkaVersion = "2.4.4"
  val circeVerion = "0.4.1"
  val slickVersion = "3.1.1"

  Seq(
    "com.typesafe.akka" %% "akka-actor"      % akkaVersion,
    "com.typesafe.akka" %% "akka-http-core"  % akkaVersion,
    "de.heikoseeberger" %% "akka-http-circe" % "1.6.0",

    "io.circe" %% "circe-core" % circeVerion,
    "io.circe" %% "circe-generic" % circeVerion,
    "io.circe" %% "circe-parser" % circeVerion,

    "net.cakesolutions" %% "scala-kafka-client-akka" % "0.7.0",

    "com.typesafe.slick" %% "slick" % slickVersion,
    "org.postgresql" % "postgresql" % "9.4-1201-jdbc41",
    "com.zaxxer" % "HikariCP" % "2.4.5",

    "com.twitter" %% "storehaus-core" % "0.13.0"
  )
}