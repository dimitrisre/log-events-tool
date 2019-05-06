package com.interview.thriftclient

import akka.actor.{ActorSystem, Props}
import com.interview.thriftclient.TCPClient.TCPClientConnectionManager
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging

/**
  * @author ${user.name}
  */
object ThriftClientApp extends App with LazyLogging {
  logger.info("Starting up Thrift Client")

  val config = ConfigFactory.load("application.conf").getConfig("app")

  val server = config.getString("server")
  val port   = config.getInt("port")

  val system = ActorSystem()
  val tcpClient = system.actorOf(Props(classOf[TCPClientConnectionManager], server, port))
}