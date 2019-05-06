package com.interview.common.myserialization

import java.util

import com.interview.common.ThriftProtocolHandler
import com.interview.common.model.logevent.{MyLogEvent, Operation}
import org.apache.kafka.common.serialization.Deserializer

class MyEventLogDeserializer extends Deserializer[Operation]{
  override def configure(map: util.Map[String, _], b: Boolean): Unit = {}

  override def deserialize(s: String, bytes: Array[Byte]): Operation = {
    val decoded = ThriftProtocolHandler.deserialize(new Operation(), bytes)
    decoded.get
  }

  override def close(): Unit = {}
}
