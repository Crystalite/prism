package xyz.crystalite.prism.services

import akka.actor.Actor
import akka.util.Timeout
import xyz.crystalite.prism.models.db.{TokenEntity, TokenEntityTable}
import xyz.crystalite.prism.models.messages.Messages._
import xyz.crystalite.prism.services.DatabaseService._
import xyz.crystalite.prism.services.DatabaseService.driver.api._
import xyz.crystalite.prism.util.Config

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class AuthActorService extends Actor with TokenEntityTable with Config {
  implicit val timeout = Timeout(5 seconds)

  // TODO: Implement a cache system to store the tokens
  override def receive: Receive = {
    case AuthMessage(token) => Await.result(authenticate(token), timeout.duration) match {
      case Some(user) => sender() ! AuthOk(user.userId, user.token)
      case None => sender() ! AuthNotFound(token)
    }
  }

  def authenticate(token: String): Future[Option[TokenEntity]] =
    db.run((for {
      user <- tokens.filter(_.token === token)
    } yield user).result.headOption)
}
