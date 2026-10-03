package repositorios

import Enums.EstadoAdministracion
import `trait`.Repositorio
import modelo.Administracion
import scala.collection.mutable.ListBuffer

class AdministradoresRepositorio extends Repositorio[Administracion] {
  override def guardar(entidad: Administracion): Unit = {

    val SQL =
      """
        |INSERT INTO Administracion(usuario,password,nombre,estado) VALUES (?,?,?,?)
        |""".stripMargin


    ejecutarActualizacion(SQL){ps =>
      ps.setString(1, entidad.usuario)
      ps.setString(2, entidad.password)
      ps.setString(3, entidad.nombre)
      ps.setString(4, entidad.estado.toString)
    }
  }
  override def listar(): List[Administracion] = {

    val SQL = "SELECT * FROM administracion"

    ejecutarConsulta(SQL) { rs =>
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
  }

  override def eliminar(id: Int): Unit = {

    val SQL =
      """
        |DELETE FROM Administracion
        |WHERE id = ?
        |""".stripMargin

    ejecutarActualizacion(SQL){ps =>
      ps.setInt(1, id)
    }
  }

}