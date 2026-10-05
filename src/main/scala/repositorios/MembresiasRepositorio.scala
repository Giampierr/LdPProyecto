package repositorios

import `trait`.Repositorio
import modelo.{Membresia, Socio}
import Enums.{EstadoSocio, TipoDocumento}
import java.time.LocalDate
import scala.collection.mutable.ListBuffer

class MembresiasRepositorio extends Repositorio[Membresia] {

  override def guardar(entidad: Membresia): Unit = {

    val SQL =
      """
        |INSERT INTO membresias(
        |idSocio,
        |fechaInicio,
        |fechaFin,
        |precio,
        |estado
        |)
        |VALUES (?, ?, ?, ?, ?)
        |""".stripMargin

    ejecutarActualizacion(SQL){ ps =>
      entidad.socio.id match {
        case Some(id) =>
          
          ps.setInt(1,id)
          ps.setString(2, entidad.fechaInicio.toString)
          ps.setString(3, entidad.fechaFin.toString)
          ps.setBigDecimal(
            4,
            entidad.precio.bigDecimal
          )
          ps.setString(
            5,
            entidad.estado.toString
          )
        case None =>
          throw new IllegalArgumentException("El socio debe estar registrado antes de crear una membresia")
      }
    }
  }
  
  override def listar(): List[Membresia] = {

    val SQL =
      """
        |SELECT
        |    m.id,
        |    m.idSocio,
        |    m.fechaInicio,
        |    m.fechaFin,
        |    m.precio,
        |    m.estado,
        |
        |    s.codigo,
        |    s.nombre,
        |    s.apellido,
        |    s.tipoDocumento,
        |    s.nroDocumento,
        |    s.telefono,
        |    s.email,
        |    s.fechaRegistro
        |
        |FROM membresias m
        |INNER JOIN socios s ON m.idSocio = s.id
        |""".stripMargin

    ejecutarConsulta(SQL){rs =>

      val membresias = ListBuffer.empty[Membresia]

      while (rs.next()) {

        val socio = Socio(
          id = Some(rs.getInt("idSocio")),
          codigo = rs.getString("codigo"),
          nombre = rs.getString("nombre"),
          apellido = rs.getString("apellido"),
          tipoDocumento = TipoDocumento.valueOf(
            rs.getString("tipoDocumento")
          ),
          nroDocumento = rs.getString("nroDocumento"),
          telefono = rs.getString("telefono"),
          email = rs.getString("email"),
          fechaRegistro = LocalDate.parse(
            rs.getString("fechaRegistro")
          )
        )

        val membresia = Membresia(
          id = Some(rs.getInt("id")),
          socio = socio,
          fechaInicio = LocalDate.parse(
            rs.getString("fechaInicio")
          ),
          fechaFin = LocalDate.parse(
            rs.getString("fechaFin")
          ),
          precio = BigDecimal(
            rs.getBigDecimal("precio")
          ),
          estado = EstadoSocio.valueOf(
            rs.getString("estado")
          )
        )

        membresias += membresia
      }
      membresias.toList
    }

  }

  override def eliminar(id: Int): Unit = {

    val SQL =
      """
        |DELETE FROM membresias
        |WHERE id = ?
        |""".stripMargin

    ejecutarActualizacion(SQL){ ps =>
      ps.setInt(1, id)

    }
  }
}