package util

import Enums.TipoDocumento
import Enums.TipoDocumento.{CarnetExtranjeria, Dni}

object Validador {

  def validarCarnetExtranjeria(carnet : String) : Boolean =
    carnet.matches("\\d{9}")

  def validarDni(dni : String) : Boolean =
    dni.matches("\\d{8}")

  def validarDocumento(tipo : TipoDocumento,numero :String) : Boolean =
    tipo match {
      case Dni =>
        validarDni(numero)
      case CarnetExtranjeria =>
        validarCarnetExtranjeria(numero)
    }

  def validarTelefono(telefono : String) : Boolean =
    telefono.matches("9\\d{8}")

  def validarCorreo(correo : String) : Boolean =
    correo.matches("^[\\w.-]+@[\\w.-]+\\.\\w+$")
}
