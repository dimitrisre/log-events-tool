package com.interview

import java.net.InetSocketAddress

import akka.actor.{Actor, Props}
import akka.io.{IO, Tcp}
import akka.io.Tcp.{Bind, Bound, Connected, ConnectionClosed, Received, Register, Write}
import akka.util.ByteString
import com.interview.common.{KafkaProducerOps, ThriftProtocolHandler}
import com.interview.common.model.logevent.{MyLogEvent, Operation}
import com.typesafe.scalalogging.LazyLogging
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

object TCPServer {

  class TCPServerConnectionManager(host: String, port: Int) extends Actor with LazyLogging {

    import context.system

    IO(Tcp) ! Bind(self, new InetSocketAddress(host, port))

    override def receive: Receive = {
      case Bound(local) =>
        logger.info(s"Server started on $local")
      case Connected(remote, local) =>
        val producer = KafkaProducerOps.getProducer

        val handler = context.actorOf(Props(classOf[TCPServerConnectionHandler], producer))
        logger.info(s"New connnection: $local -> $remote")

        sender() ! Register(handler)
    }
  }

  class TCPServerConnectionHandler(producer: KafkaProducer[String, Operation]) extends Actor with LazyLogging {
    override def receive: Receive = {
      case Received(data) =>
        val decoded = ThriftProtocolHandler.deserialize(new Operation(), data.toArray[Byte])
        logger.info("Received from client: {}", decoded.get)
        producer.send(new ProducerRecord(KafkaProducerOps.topic,"logentry",decoded.get))

        sender() ! Write(ByteString(s"Received: $decoded"))
      case message: ConnectionClosed =>
        logger.info("Connection has been closed")
        context stop self
    }
  }
}