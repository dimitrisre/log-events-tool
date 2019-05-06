package com.interview.thriftserver

import akka.actor._
import com.interview.TCPServer.TCPServerConnectionManager
import com.typesafe.scalalogging.LazyLogging
import com.typesafe.config.ConfigFactory

/**
  * @author ${user.name}
  */
object ThriftServerApp extends App with LazyLogging {

  logger.info("Starting up Thrift Server")

  val config = ConfigFactory.load("application.conf").getConfig("app")

  val host = config.getString("host")
  val port = config.getInt("port")

  val system = ActorSystem()
  val tcpServer = system.actorOf(Props(classOf[TCPServerConnectionManager], host, port))

}
