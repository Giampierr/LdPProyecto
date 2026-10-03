package modelo

import Enums.TipoDocumento
import util.Validador

import java.time.LocalDate

case class Socio(
                id : Option[Int],
                codigo : String,
                nombre : String,
                apellido : String,
                tipoDocumento: TipoDocumento,
                nroDocumento : String,
                telefono : String,
                email : String,
                fechaRegistro : LocalDate
                ){
  require(codigo.nonEmpty)
  require(nombre.nonEmpty)
  require(apellido.nonEmpty)
  require(Validador.validarDocumento(tipoDocumento,nroDocumento),"Nro inválido")
  require(Validador.validarTelefono(telefono),"Telefono inválido")
  require(Validador.validarCorreo(email),"Correo inválido")
  require(!fechaRegistro.isAfter(LocalDate.now()),"La fecha no puede ser futura")
}
object Socio {
  def nueva(
           codigo : String,
           nombre : String,
           apellido : String,
           tipoDocumento : TipoDocumento,
           nroDocumento : String,
           telefono : String,
           email : String,
           fechaRegistro : LocalDate
           ) : Socio =
    Socio(
      None,
      codigo,
      nombre,
      apellido,
      tipoDocumento,
      nroDocumento,
      telefono,
      email,
      fechaRegistro
    )
}
