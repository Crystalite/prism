package xyz.crystalite.prism.models.db

case class TokenEntity(id: Option[Long] = None, userId: String, token: String) {
  require(!userId.isEmpty, "username.empty")
  require(!token.isEmpty, "password.empty")
}

