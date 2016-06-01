package xyz.crystalite.prism.services.actors

import java.util.concurrent.TimeUnit

import akka.actor.Actor
import akka.util.Timeout
import com.twitter.storehaus.cache.TTLCache
import xyz.crystalite.prism.models.db.UsersEntityTable
import xyz.crystalite.prism.models.messages.Messages._
import xyz.crystalite.prism.services.DatabaseService._
import xyz.crystalite.prism.services.DatabaseService.driver.api._
import xyz.crystalite.prism.util.Config

import com.twitter.util.Duration
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class AuthActor extends Actor with UsersEntityTable with Config {
  implicit val timeout = Timeout(5 seconds)

  var cache = TTLCache[String, Long](Duration(15, TimeUnit.MINUTES))

  override def receive: Receive = {
    case AuthMessage(token) => println(cache.toNonExpiredMap)
      authenticate(token) match {
        case Some(id) =>
          cache = cache.putClocked(token -> id)._2
          sender() ! AuthOk(id, token)
        case None => sender() ! AuthNotFound(token)
      }
  }

  def authenticate(token: String): Option[Long] = {
    cache.toNonExpiredMap.get(token) match {
      case Some(k) => Some(k)
      case _ =>
        Await.result[Option[Long]](
          db.run((for {user <- users.filter(_.token === token)} yield user).result.headOption).map {
            case Some(x) => x.id
            case _ => None
          }, timeout.duration)
    }
  }

}

