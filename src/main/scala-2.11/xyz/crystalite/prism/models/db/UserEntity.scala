package xyz.crystalite.prism.models.db

case class UserEntity(id: Option[Long] = None, token: String) {
  require(!token.isEmpty, "token.empty")
}

