package `trait`

import util.Conexion

import java.sql.{PreparedStatement, ResultSet, SQLException}

trait Repositorio [T]{

   def ejecutarConsulta[A](
                                     sql: String,
                                     configurar: PreparedStatement => Unit = _ => ()
                                   )(procesar: ResultSet => A): A = {

    val cn = Conexion.conectar()
    val ps = cn.prepareStatement(sql)

    try {

      configurar(ps)

      val rs = ps.executeQuery()

      try {
        procesar(rs)
      } finally {
        rs.close()
      }

    } catch {
      case e: SQLException =>
        println(s"Error en la consulta: ${e.getMessage}")
        throw e

    } finally {
      ps.close()
      cn.close()
    }
  }

  def ejecutarActualizacion(sql :String)(configurar : PreparedStatement =>Unit):Unit ={
    val cn = Conexion.conectar()
    val ps = cn.prepareStatement(sql)
    try{
      configurar(ps)
      ps.executeUpdate()
    }catch {
      case e:SQLException =>
        println(s"Error al actualizar : ${e.getMessage}")
        throw e
    }
    finally {
      cn.close()
      ps.close()
    }
  }
  def guardar(entidad : T) :Unit
  def listar():List[T]
  def eliminar(id : Int): Unit
}
