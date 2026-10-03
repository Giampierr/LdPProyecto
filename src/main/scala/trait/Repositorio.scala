package `trait`

import util.Conexion

import java.sql.{ResultSet, SQLException}

trait Repositorio [T]{
  def ejecutarConsulta[A](sql :String)(procesar : ResultSet =>A):A ={
    val cn = Conexion.conectar()
    val ps = cn.prepareStatement(sql)
    val rs = ps.executeQuery()
    
    try{
      procesar(rs)
    }catch {
      case e:SQLException =>
        println(s"Error en la consulta : ${e.getMessage}")
        throw e
    }finally {
      rs.close()
      ps.close()
      cn.close()
    }
  }
  def guardar(entidad : T) :Unit
  def listar():List[T]
  def eliminar(id : Int): Unit
}
