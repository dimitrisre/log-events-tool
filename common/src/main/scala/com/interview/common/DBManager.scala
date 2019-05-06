package com.interview.common

import com.datastax.driver.core.Cluster
import com.typesafe.config.ConfigFactory

object DBManager {
  val config = ConfigFactory.load("cassandra.conf").getConfig("cassandra")

  val host     = config.getString("server")
  val keyspace = config.getString("keyspace")

  val cluster = Cluster.builder()
    .addContactPoint(host)
    .build()

  val session = cluster.connect(keyspace)

  def getSession = session
}
