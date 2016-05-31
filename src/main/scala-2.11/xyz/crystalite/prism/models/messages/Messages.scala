package xyz.crystalite.prism.models.messages

object Messages {

  case class UserMessage(token: String, payload: String) {
    require(!token.isEmpty, "token.empty")
    require(!payload.isEmpty, "payload.empty")
  }

  // Auth Messages
  case class AuthMessage(token: String)
  case class AuthOk(userID: String, token: String)
  case class AuthNotFound(token: String, msg: String = "The user dosen't exist or it's invalid")

}
