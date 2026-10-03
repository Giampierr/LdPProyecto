package repositorios

import `trait`.Repositorio
import modelo.Socio
import util.Conexion
import Enums.TipoDocumento

import java.sql.SQLException
import java.time.LocalDate
import scala.collection.mutable.ListBuffer

class SocioRepositorios extends Repositorio[Socio] {

  override def guardar(entidad: Socio): Unit = {

    val SQL =
      """
        |INSERT INTO socios(
        |codigo,
        |nombre,
        |apellido,
        |tipoDocumento,
        |nroDocumento,
        |telefono,
        |email,
        |fechaRegistro
        |)
        |VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        |""".stripMargin

    try {

      val cn = Conexion.conectar()
      val ps = cn.prepareStatement(SQL)

      ps.setString(1, entidad.codigo)
      ps.setString(2, entidad.nombre)
      ps.setString(3, entidad.apellido)
      ps.setString(4, entidad.tipoDocumento.toString)
      ps.setString(5, entidad.nroDocumento)
      ps.setString(6, entidad.telefono)
      ps.setString(7, entidad.email)
      ps.setString(8, entidad.fechaRegistro.toString)

      ps.executeUpdate()

      ps.close()
      cn.close()

    } catch {
      case e: SQLException =>
        println(s"Error al guardar socio: ${e.getMessage}")
    }
  }

  override def listar(): List[Socio] = {

    val SQL = "SELECT * FROM socios"
    
    ejecutarConsulta(SQL) { rs =>

      val socios = ListBuffer.empty[Socio]

      while (rs.next()) {

        val socio = Socio(
          Some(rs.getInt("id")),
          rs.getString("codigo"),
          rs.getString("nombre"),
          rs.getString("apellido"),
          TipoDocumento.valueOf(
            rs.getString("tipoDocumento")
          ),
          rs.getString("nroDocumento"),
          rs.getString("telefono"),
          rs.getString("email"),
          LocalDate.parse(
            rs.getString("fechaRegistro")
          )
        )

        socios += socio
      }      
      socios.toList

    }
    
  }

  override def eliminar(id: Int): Unit = {

    val SQL =
      """
        |DELETE FROM socios
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
        println(s"Error al eliminar socio: ${e.getMessage}")
    }
  }

  def buscarPorId(idSocio: Int): Option[Socio] = {

    val SQL =
      """
        |SELECT * FROM socios
        |WHERE id = ?
        |""".stripMargin

    try {

      val cn = Conexion.conectar()
      val ps = cn.prepareStatement(SQL)

      ps.setInt(1, idSocio)

      val rs = ps.executeQuery()

      val socio =
        if (rs.next()) {

          Some(
            Socio(
              Some(rs.getInt("id")),
              rs.getString("codigo"),
              rs.getString("nombre"),
              rs.getString("apellido"),
              TipoDocumento.valueOf(
                rs.getString("tipoDocumento")
              ),
              rs.getString("nroDocumento"),
              rs.getString("telefono"),
              rs.getString("email"),
              LocalDate.parse(
                rs.getString("fechaRegistro")
              )
            )
          )

        } else {
          None
        }

      rs.close()
      ps.close()
      cn.close()

      socio

    } catch {
      case e: SQLException =>
        println(
          s"Error al buscar socio con id $idSocio: ${e.getMessage}"
        )

        None
    }
  }
}