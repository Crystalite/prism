package xyz.crystalite.prism.messages

object Messages {
  case class UserMessage(token: String, payload: String) {
    require(!token.isEmpty, "token.empty")
    require(!payload.isEmpty, "payload.empty")
  }

  // Auth Messages
  case class AuthMessage(userID: String, token: String)
  case class AuthOk(userID: String)
  case class AuthNotFound(userdId: String, msg: String = "The user dosen't exist or it's invalid")
  case class AuthInvalid(userdId: String, token: String, msg: String = "The token dosen't exist or it's invalid")
}
