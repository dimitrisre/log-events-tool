package com.interview.kafkaconsumer

import java.util.{Date, UUID}

import com.interview.common.dao.{LogEvent, LogEventDAO}
import com.interview.common.{DBManager, KafkaConsumerOps}
import com.typesafe.scalalogging.LazyLogging

import scala.collection.JavaConverters._
/**
  * @author ${user.name}
  */
object KafkaConsumerApp extends App with LazyLogging{
  val consumer = KafkaConsumerOps.getConsumer
  val session  = DBManager.getSession
  val logEventDAO = LogEventDAO(session)
  val date = new Date()
  while(true){
    val records = consumer.poll(10000L).asScala
    for(record <- records){
      logger.info("----------------------------->Consumed this record: {}", record)
      val payload = record.value.payload
      logEventDAO.insert(LogEvent(payload.v, payload.time, payload.m, UUID.randomUUID()))

      val listLogs = logEventDAO.getAll
      logger.info("Cassandra dump")
      listLogs.foreach(
        log => logger.info("------------>{}",log)
      )
      logger.info("--------------")
      consumer.commitSync()
    }
  }
}