package xyz.crystalite.prism.services

import java.io.Serializable

import akka.actor.Actor
import akka.util.Timeout
import xyz.crystalite.prism.models.db.{UserEntity, UsersEntityTable}
import xyz.crystalite.prism.models.messages.Messages._
import xyz.crystalite.prism.services.DatabaseService._
import xyz.crystalite.prism.services.DatabaseService.driver.api._
import xyz.crystalite.prism.util.Config

import scala.collection.mutable
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global

class AuthActorService extends Actor with UsersEntityTable with Config {
  implicit val timeout = Timeout(5 seconds)

  val state = mutable.Map[String, Long]()

  override def receive: Receive = {
    case AuthMessage(token) => println(state)
      authenticate(token) match {
        case Some(id) =>
          state += (token -> id)
          sender() ! AuthOk(id, token)
        case None => sender() ! AuthNotFound(token)
      }
  }

  def authenticate(token: String): Option[Long] = {
    state.getOrElse(token, Await.result[Option[Long]](
      db.run((for {user <- users.filter(_.token === token)} yield user).result.headOption).map {
        case Some(x) => x.id
        case _ => None
      }, timeout.duration)) match {
      case x: Long => Some(x)
      case Some(x: Long) => Some(x)
      case _ => None
    }
  }


}

