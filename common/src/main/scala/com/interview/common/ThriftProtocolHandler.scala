package com.interview.common

import akka.util.ByteString
import com.interview.common.model.logevent.Operation
import com.typesafe.scalalogging.LazyLogging

import scala.util.Try
import org.apache.thrift._

object ThriftProtocolHandler extends LazyLogging{

  def deserialize[T <: TBase[_ <: TBase[_, _], _ <: TFieldIdEnum]](obj: T, bytes: Array[Byte]): Option[T] =
    Try {
      val deSerializer = new TDeserializer()

      obj match {
        case _:  Operation => deSerializer.deserialize(obj, bytes)
        case _ => logger.error("Object is not a thrift message")
      }
      Some(obj)
    } getOrElse {
      logger.warn("Failed to deserialize object of type {}. Byte size {}", obj.getClass, s"${bytes.length}")
      None
    }

  def serialize[T<: TBase[_ <: TBase[_, _], _ <: TFieldIdEnum]](obj: T): Option[ByteString] = {
    Try{
      val serializer = new TSerializer()
      Some(ByteString(serializer.serialize(obj)))
    } getOrElse {
      logger.warn("Cannot serialize object {}",obj)
      None
    }
  }
}
