package cn.whu.ypfamily.EMGeoStreaming.utils

import java.sql.{Connection, DriverManager}
import java.util

object ConnectionPool {
  private val max = 20 // Max number of connections in the pool
  private val connectionNum = 5 // The number of connections created every time
  private var conNum = 0 // The number of connections exist in the pool
  private val pool = new util.LinkedList[Connection]() // Connection pool

  // Get Jdbc connection
  def getJdbcConn(connUrl: String, acc: String, psw: String): Connection = {
    // Synchronize
    AnyRef.synchronized({
      if (pool.isEmpty) {
        // Load driver
        preGetConn()
        for (i <- 1 to connectionNum) {
          val conn = DriverManager.getConnection(connUrl, acc, psw)
          pool.push(conn)
          conNum += 1
        }
      }
      pool.poll()
    })
  }

  // Release connection
  def releaseConn(conn: Connection): Unit = {
    pool.push(conn)
  }

  // Get connection number
  def getConNum(): Int = {
    conNum
  }

  // Load driver
  private def preGetConn(): Unit = {
    // Control loading
    if (conNum > max && pool.isEmpty) {
      println("Jdbc Pool has no connection now, please wait a moments!")
      Thread.sleep(2000)
      preGetConn()
    } else {
      Class.forName("org.postgresql.Driver")
    }
  }

}
