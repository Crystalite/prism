package xyz.crystalite.prism.util

import com.typesafe.config.ConfigFactory

trait Config {
  private val config = ConfigFactory.load()
  private val services = config.getConfig("services")

  val httpConfig = services.getConfig("http")
  val zkConfig = services.getConfig("zookeeper")
  val kafkaConfig = services.getConfig("kafka")
  val dbConfig = services.getConfig("db")
}
