package com.interview.thriftclient

import java.net.InetSocketAddress
import java.util.Date

import akka.actor.{Actor, ActorRef, Props}
import akka.io.{IO, Tcp}
import akka.io.Tcp.{Close, CommandFailed, Connect, Connected, ConnectionClosed, Received, Register, Write}
import akka.util.ByteString
import com.interview.common.ThriftProtocolHandler
import com.interview.common.model.logevent.{MyLogEvent, Operation, OperationType}
import com.typesafe.scalalogging.LazyLogging

import scala.util.Random

object TCPClient {
  class TCPClientConnectionManager(host: String, port: Int) extends Actor with LazyLogging{
    import context.system
    val date = new Date()
    val randomGenerator = new Random(date.getTime)
    IO(Tcp) ! Connect(new InetSocketAddress(host, port))

    def receive: Receive = {
      case CommandFailed(_: Connect) =>
        logger.info("Connection failed")
        context stop self
      case connected@Connected(remote, local) =>
        //listener ! connected
        logger.info("Connected to remote server")
        val connection = sender()
        val handler = context.actorOf(Props(classOf[TCPClientHandler], connection))
        connection ! Register(handler)
        handler ! ThriftProtocolHandler.serialize(
          new Operation(
            OperationType.INSERT,
            new MyLogEvent(1, date.getTime, randomGenerator.alphanumeric.take(50).mkString)
          )
        ).get
    }
  }

  class TCPClientHandler(connection: ActorRef) extends Actor with LazyLogging {

    def receive: Receive = {
      //    case th: MyLogEvent =>
      //      connection ! Write(th)
      case data: ByteString =>
        logger.info("Sending this message to Server: {}", data)
        connection ! Write(data)
      case CommandFailed(w: Write) =>
        logger.info("Write failed")
      case Received(data) =>
        logger.info("Received {}", data)
      case "close" =>
        connection ! Close
      case _: ConnectionClosed =>
        logger.info("connection closed")
        context stop self
    }
  }
}
