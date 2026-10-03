package repositorios

import Enums.EstadoAdministracion
import `trait`.Repositorio
import modelo.Administracion
import util.Conexion

import java.sql.SQLException
import scala.collection.mutable.ListBuffer

class AdministradoresRepositorio extends Repositorio[Administracion] {
  override def guardar(entidad: Administracion): Unit = {

    val SQL =
      """
        |INSERT INTO Administracion(usuario,password,nombre,estado) VALUES (?,?,?,?)
        |""".stripMargin


    try {
      val cn = Conexion.conectar()
      val ps = cn.prepareStatement(SQL)

      ps.setString(1, entidad.usuario)
      ps.setString(2, entidad.password)
      ps.setString(3, entidad.nombre)
      ps.setString(4, entidad.estado.toString)

      ps.executeUpdate()

      ps.close()
      cn.close()

    } catch {
      case e: SQLException =>
        println(s"Error al guardar administracion : ${e.getMessage}")
    }
  }
  override def listar(): List[Administracion] = {

    val SQL = "SELECT * FROM administracion"

    try {
      val cn = Conexion.conectar()
      val ps = cn.prepareStatement(SQL)
      val rs = ps.executeQuery()

      val administradores = ListBuffer.empty[Administracion]

      ejecutarConsulta(SQL){rs =>
        val administradores = ListBuffer.empty[Administracion]
        while (rs.next()) {
          val admin = Administracion(
            Some(rs.getInt("id")),
            rs.getString("usuario"),
            rs.getString("password"),
            rs.getString("nombre"),
            EstadoAdministracion.valueOf(
              rs.getString("estado")
            )
          )

          administradores += admin
        }
        administradores.toList
      }


    } catch {
      case e: SQLException =>
        println(s"Error al leer administraciones: ${e.getMessage}")
        List.empty[Administracion]
    }
  }

  override def eliminar(id: Int): Unit = {

    val SQL =
      """
        |DELETE FROM Administracion
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
        println(s"Error al eliminar administración: ${e.getMessage}")
    }
  }

}