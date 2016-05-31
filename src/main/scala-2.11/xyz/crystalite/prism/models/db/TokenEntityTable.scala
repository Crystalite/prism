package xyz.crystalite.prism.models.db

import slick.lifted.TableQuery
import xyz.crystalite.prism.services.DatabaseService.driver.api._

trait TokenEntityTable {

  class Tokens(tag: Tag) extends Table[TokenEntity](tag, "tokens") {
    def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
    def userId = column[String]("user_id")
    def token = column[String]("token")

    def * = (id, userId, token) <> ((TokenEntity.apply _).tupled, TokenEntity.unapply)
  }

  protected val tokens = TableQuery[Tokens]


}
