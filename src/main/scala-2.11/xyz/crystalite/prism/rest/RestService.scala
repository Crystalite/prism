package xyz.crystalite.prism.rest

import akka.actor.ActorRef
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import cakesolutions.kafka.akka.ProducerRecords
import akka.pattern.ask
import io.circe.generic.auto._
import io.circe.syntax._
import de.heikoseeberger.akkahttpcirce.CirceSupport
import akka.util.Timeout
import xyz.crystalite.prism.kafka.KafkaService
import xyz.crystalite.prism.messages.Messages._

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext}

class RestService(authActor: ActorRef,
                  kafkaProducer: ActorRef
                 )(implicit executionContext: ExecutionContext) extends CirceSupport {

  implicit val timeout = Timeout(5 seconds)

  val routes =
    pathPrefix("v1") {
      pathPrefix(JavaUUID) { userID =>
        pathEndOrSingleSlash {
          post {
            entity(as[UserMessage]) { message =>
              val authFuture = authActor ? AuthMessage(userID.toString, message.token)

              complete(Await.result(authFuture, Timeout(5 seconds).duration) match {
                case AuthOk(id) =>
                  kafkaProducer !
                    ProducerRecords
                      .fromValuesWithKey[String, String](KafkaService.topic, id, Seq(message.payload), None)
                  OK
                case msg: AuthNotFound => NotFound -> (msg asJson)
                case msg: AuthInvalid => Unauthorized -> (msg asJson)
              })
            }
          }
        }
      }
    }
}

