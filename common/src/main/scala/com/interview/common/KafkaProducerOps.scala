package com.interview.common

import java.util.Properties

import com.interview.common.model.logevent.{MyLogEvent, Operation}
import com.typesafe.config.ConfigFactory
import org.apache.kafka.clients.producer.KafkaProducer

object KafkaProducerOps {
  val config = ConfigFactory.load("kafka.conf").getConfig("kafka")

  val topic             = config.getString("topic")
  val kafkaServer       = config.getString("bootstrap.servers")
  val keySerializer     = config.getString("key.serializer")
  val valueSerializer   = config.getString("value.serializer")

  val props = new Properties()

  props.put("bootstrap.servers",  kafkaServer)
  props.put("key.serializer",   keySerializer)
  props.put("value.serializer", valueSerializer)

  def getProducer: KafkaProducer[String, Operation] = new KafkaProducer[String, Operation](props)

}
