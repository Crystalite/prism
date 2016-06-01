package xyz.crystalite.prism.models.db

import slick.lifted.TableQuery
import xyz.crystalite.prism.services.DatabaseService.driver.api._

trait UsersEntityTable {

  class Users(tag: Tag) extends Table[UserEntity](tag, "users") {
    def id = column[Option[Long]]("id", O.AutoInc, O.PrimaryKey)
    def token = column[String]("token")

    def * = (id, token) <> ((UserEntity.apply _).tupled, UserEntity.unapply)
  }

  protected val users = TableQuery[Users]
}
