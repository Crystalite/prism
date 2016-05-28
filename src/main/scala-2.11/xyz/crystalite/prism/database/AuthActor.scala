package xyz.crystalite.prism.database

import akka.actor.Actor
import xyz.crystalite.prism.messages.Messages.{AuthInvalid, AuthMessage, AuthNotFound, AuthOk}

class AuthActor extends Actor {
  override def receive: Receive = {
    case AuthMessage(userID, token) => checkDB(userID, token) match {
      case  0 => sender() ! AuthOk(userID)
      case -1 => sender() ! AuthNotFound(userID)
      case -2 => sender() ! AuthInvalid(userID, token)
    }
  }

  def checkDB(userID: String, token: String): Int = {
    0
  }
}
