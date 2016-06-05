package xyz.crystalite.prism.services.actors

import java.util.concurrent.TimeUnit

import akka.actor.Actor
import akka.util.Timeout
import com.twitter.storehaus.cache.TTLCache
import com.twitter.util.Duration
import xyz.crystalite.prism.models.db.UsersEntityTable
import xyz.crystalite.prism.models.messages.Messages._
import xyz.crystalite.prism.services.DatabaseService._
import xyz.crystalite.prism.services.DatabaseService.driver.api._
import xyz.crystalite.prism.util.Config

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.util.{Success, Failure}

class AuthActor extends Actor with UsersEntityTable with Config {
  implicit val timeout = Timeout(30 seconds)

  var cache = TTLCache[String, Long](Duration(15, TimeUnit.MINUTES))

  override def receive: Receive = {
    case AuthMessage(token) =>
      val senderRef = sender()
      cache.toNonExpiredMap.get(token) match {
        case Some(id) => senderRef ! AuthOk(id, token)
        case _ =>
          db.run((for {user <- users.filter(_.token === token)} yield user).result.headOption).map {
            case Some(x) => x.id
            case _ => None
          } onComplete {
            case Success(idOpts) =>
              idOpts match {
                case Some(id) =>
                  cache = cache.putClocked(token -> id)._2
                  senderRef ! AuthOk(id, token)
                case _ => senderRef ! AuthNotFound(token)
              }
            case Failure(e) => senderRef! AuthNotFound(token, msg = e.getMessage)
          }
      }
  }
}


