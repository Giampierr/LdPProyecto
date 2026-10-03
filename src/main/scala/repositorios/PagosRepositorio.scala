package repositorios

import `trait`.Repositorio
import modelo.Pago
import scala.collection.mutable.ListBuffer

class PagosRepositorio extends Repositorio[Pago] {

  override def guardar(entidad: Pago): Unit = {

    val SQL =
      """
        |INSERT INTO PAGOS(fecha, monto)
        |VALUES (?, ?)
        |""".stripMargin

    ejecutarActualizacion(SQL){ ps =>
      ps.setString(1, entidad.fecha.toString)

      ps.setBigDecimal(
        2,
        entidad.monto.bigDecimal
      )

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

    ejecutarActualizacion(SQL){ ps =>
      ps.setInt(1, id)
    }
  }
}