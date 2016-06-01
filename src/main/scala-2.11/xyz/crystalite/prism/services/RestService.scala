package xyz.crystalite.prism.services

import akka.actor.ActorRef
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.util.Timeout
import cakesolutions.kafka.akka.ProducerRecords
import de.heikoseeberger.akkahttpcirce.CirceSupport
import io.circe.generic.auto._
import io.circe.syntax._
import xyz.crystalite.prism.models.messages.Messages._

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext}

class RestService(authActor: ActorRef,
                  kafkaProducer: ActorRef
                 )(implicit executionContext: ExecutionContext) extends CirceSupport {

  implicit val timeout = Timeout(10 seconds)

  val routes =
    pathEndOrSingleSlash {
      post {
        entity(as[UserMessage]) { message =>
          val authFuture = authActor ? AuthMessage(message.token)

          complete(Await.result(authFuture, timeout.duration) match {
            case AuthOk(id, token) =>
              kafkaProducer ! ProducerRecords
                .fromValuesWithKey[String, String](KafkaService.topic, id.toString, Seq(message.payload), None);
              OK
            case msg: AuthNotFound => NotFound -> (msg asJson)
          })
        }
      }
    }
}

