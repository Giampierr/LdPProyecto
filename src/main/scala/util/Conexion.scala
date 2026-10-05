package util

import java.sql.{Connection, DriverManager}

object Conexion {
  private val URL = "jdbc:sqlite:src/main/scala/database/gimnasia.db"
  
  def conectar() : Connection = {
      DriverManager.getConnection(URL)
  }
}
