package xyz.crystalite.prism

import akka.actor.{ActorSystem, Props}
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import xyz.crystalite.prism.database.AuthActor
import xyz.crystalite.prism.kafka.KafkaService
import xyz.crystalite.prism.rest.RestService

import scala.concurrent.ExecutionContext

object Prism extends App {

  implicit val system = ActorSystem("prism-system")
  implicit val executor: ExecutionContext = system.dispatcher
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val kafkaProducer = system.actorOf(KafkaService.actorProps)
  val authenticator = system.actorOf(Props[AuthActor])

  val restService = new RestService(authenticator, kafkaProducer)

  val bindingFuture = Http().bindAndHandle(restService.routes, "localhost", 8080)
  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
}
