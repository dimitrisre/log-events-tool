package com.interview.common.dao

import java.util.UUID

import com.datastax.driver.core.Session
import com.datastax.driver.core.querybuilder.{QueryBuilder => QB}

case class LogEvent(version: Short, timestamp: Long, message: String, id: UUID)

case class LogEventDAO(session: Session) {
  def createTable = {
    session.execute(
      """CREATE TABLE IF NOT EXISTS logevents (
         version smallint,
         timestamp bigint,
         message string,
         id uuid,
         PRIMARY KEY (id)
     );""".stripMargin)
  }
  def getAll: List[LogEvent] = {
    val query = QB.select().all().from("pollfish", "logevents")
    val results = session.execute(query)

    var logList: List[LogEvent] = List.empty[LogEvent]

    val resultsIterator = results.all().iterator()
    while(resultsIterator.hasNext) {
      val row = resultsIterator.next()
      logList = logList :+ LogEvent(row.getShort("version"), row.getLong("timestamp"), row.getString("message"), row.getUUID("id"))
    }

    logList.reverse
  }

  def insert(logEvent: LogEvent) = {
    var query = s"""insert into logevents(version, timestamp, message, id) values (${logEvent.version}, ${logEvent.timestamp}, '${logEvent.message}', ${logEvent.id})"""
    session.execute(query)
  }
}
