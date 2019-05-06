package com.interview.thriftclient

import akka.actor.{ActorRef, ActorSystem, Props}
import com.interview.thriftclient.TCPClient.{GenerateLog, TCPClientConnectionManager}
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import java.nio.file.{Path, WatchEvent, StandardWatchEventKinds => EventType}

import better.files._
import FileWatcher._


/**
  * @author ${user.name}
  */
object ThriftClientApp extends App with LazyLogging {
  logger.info("Starting up Thrift Client")

  val config = ConfigFactory.load("application.conf").getConfig("app")

  val server = config.getString("server")
  val port   = config.getInt("port")

  implicit val system = ActorSystem()
  val tcpClient = system.actorOf(Props(classOf[TCPClientConnectionManager], server, port))

  val watcher: ActorRef = (File.home/"generate_random_logs.tmp").newWatcher(recursive = true)

  watcher ! on(EventType.ENTRY_MODIFY) {
    case file => {
      logger.info(s"$file got modified")
      tcpClient ! GenerateLog()
    }
  }
}