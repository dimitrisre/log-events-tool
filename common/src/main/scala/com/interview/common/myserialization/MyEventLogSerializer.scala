package com.interview.common.myserialization

import java.util

import com.interview.common.ThriftProtocolHandler
import com.interview.common.model.logevent.{MyLogEvent, Operation}
import org.apache.kafka.common.serialization.Serializer

class MyEventLogSerializer extends Serializer[Operation]{
  override def configure(map: util.Map[String, _], b: Boolean): Unit = {}

  override def serialize(s: String, t: Operation): Array[Byte] = {
    ThriftProtocolHandler.serialize(t).get.toArray
  }

  override def close(): Unit = {}
}
