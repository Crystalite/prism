package xyz.crystalite.prism.services

import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import xyz.crystalite.prism.util.Config

object DatabaseService extends Config {
  private val hikariConfig = new HikariConfig()
  hikariConfig.setJdbcUrl(dbConfig.getString("url"))
  hikariConfig.setUsername(dbConfig.getString("user"))
  hikariConfig.setPassword(dbConfig.getString("password"))

  private val dataSource = new HikariDataSource(hikariConfig)

  val driver = slick.driver.PostgresDriver
  import driver.api._
  val db = Database.forDataSource(dataSource)
  db.createSession()
}
