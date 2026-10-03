package modelo

import Enums.EstadoAdministracion

case class Administracion(
                         id : Option[Int],
                         usuario : String,
                         password : String,
                         nombre :String,
                         estado : EstadoAdministracion
                         ){
                          require(usuario.nonEmpty)
                          require(password.nonEmpty)
                          require(nombre.nonEmpty)

}

object Administracion {
  def nueva(
           usuario : String,
           password : String,
           nombre : String ,
           estado : EstadoAdministracion
           ): Administracion =
    Administracion(
      None,
      usuario,
      password,
      nombre , 
      estado 
    )
}
