package repositorios

import `trait`.Repositorio
import modelo.Pago
import util.Conexion

import java.sql.SQLException
import scala.collection.mutable.ListBuffer

class PagosRepositorio extends Repositorio[Pago] {

  override def guardar(entidad: Pago): Unit = {

    val SQL =
      """
        |INSERT INTO PAGOS(fecha, monto)
        |VALUES (?, ?)
        |""".stripMargin

    try {
      val cn = Conexion.conectar()
      val ps = cn.prepareStatement(SQL)

      ps.setString(1, entidad.fecha.toString)

      ps.setBigDecimal(
        2,
        entidad.monto.bigDecimal
      )

      ps.executeUpdate()

      ps.close()
      cn.close()

    } catch {
      case e: SQLException =>
        println(s"Error al guardar el pago: ${e.getMessage}")
    }
  }

  override def listar(): List[Pago] = {

    val SQL = "SELECT * FROM PAGOS"

    ejecutarConsulta(SQL){rs =>
      val pagos = ListBuffer.empty[Pago]

      while (rs.next()) {

        val pago = Pago(
          Some(rs.getInt("id")),
          rs.getTimestamp("fecha").toLocalDateTime,
          BigDecimal(rs.getBigDecimal("monto"))
        )

        pagos += pago
      }
      pagos.toList
    }


    
  }

  override def eliminar(id: Int): Unit = {

    val SQL =
      """
        |DELETE FROM PAGOS
        |WHERE id = ?
        |""".stripMargin

    try {
      val cn = Conexion.conectar()
      val ps = cn.prepareStatement(SQL)

      ps.setInt(1, id)

      ps.executeUpdate()

      ps.close()
      cn.close()

    } catch {
      case e: SQLException =>
        println(s"Error al eliminar el pago: ${e.getMessage}")
    }
  }
}