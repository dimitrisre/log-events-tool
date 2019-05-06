package com.interview.common

import java.util.{Collections, Properties}

import com.interview.common.model.logevent.{MyLogEvent, Operation}
import com.typesafe.config.ConfigFactory
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.TopicPartition

object KafkaConsumerOps {
  val config = ConfigFactory.load("kafka.conf").getConfig("kafka")

  val topic             = config.getString("topic")
  val groupId           = config.getString("group.id")
  val clientId          = config.getString("client.id")
  val kafkaServer       = config.getString("bootstrap.servers")
  val keyDeserializer   = config.getString("key.deserializer")
  val valueDeserializer = config.getString("value.deserializer")

  val props = new Properties()

  props.put("group.id",  groupId)
  props.put("client.id", clientId)
  props.put("bootstrap.servers",  kafkaServer)
  props.put("key.deserializer",   keyDeserializer)
  props.put("value.deserializer", valueDeserializer)

  def getConsumer: KafkaConsumer[String, Operation] = {
    val consumer = new KafkaConsumer[String, Operation](props)
    consumer.subscribe(Collections.singletonList(topic))
//    consumer.assign(Collections.singletonList(new TopicPartition(topic,0)))
//    consumer.seekToEnd(Collections.singletonList(new TopicPartition(topic, 0)))
    //consumer.seekToEnd()

    consumer
  }

}
