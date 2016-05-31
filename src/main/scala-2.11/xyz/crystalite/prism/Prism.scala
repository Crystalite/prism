package xyz.crystalite.prism

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import xyz.crystalite.prism.services.{AuthActorService, KafkaService, RestService}
import xyz.crystalite.prism.util.Config

import scala.concurrent.ExecutionContext

object Prism extends App with Config {

  implicit val system = ActorSystem("prism-system")
  implicit val executor: ExecutionContext = system.dispatcher
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val kafkaProducer = system.actorOf(KafkaService.actorProps)
  val authenticator = system.actorOf(Props[AuthActorService])

  val restService = new RestService(authenticator, kafkaProducer)

  val interface = httpConfig.getString("interface")
  val port = httpConfig.getInt("port")
  val bindingFuture = Http().bindAndHandle(restService.routes, interface, port)
  println(s"Server online at http://$interface:$port/")
}
