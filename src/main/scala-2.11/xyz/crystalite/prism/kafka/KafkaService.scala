package xyz.crystalite.prism.kafka

import cakesolutions.kafka.KafkaProducer
import cakesolutions.kafka.akka.KafkaProducerActor
import org.apache.kafka.common.serialization.StringSerializer

object KafkaService {
  val topic = "prism"

  val kafkaConf = KafkaProducer.Conf(
    bootstrapServers = "localhost:9092",
    keySerializer = new StringSerializer,
    valueSerializer = new StringSerializer
  )

  val actorProps = KafkaProducerActor.
    propsWithMatcher[String, String](kafkaConf, KafkaProducerActor.defaultMatcher[String, String])
}
