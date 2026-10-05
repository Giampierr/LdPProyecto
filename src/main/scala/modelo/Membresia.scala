package modelo

import java.time.LocalDate
import scala.math.BigDecimal
import Enums.{EstadoSocio, TipoMembresia}

case class Membresia(
                      id: Option[Int],
                      socio: Socio,
                      fechaInicio: LocalDate,
                      fechaFin: LocalDate,
                      precio: BigDecimal,
                      estado: EstadoSocio
                    ) {

  require(
    !fechaInicio.isAfter(LocalDate.now()),
    "La fecha de inicio no puede ser futura"
  )

  require(
    !fechaFin.isBefore(fechaInicio),
    "La fecha de fin no puede ser anterior a la fecha de inicio"
  )

  require(
    precio > 0,
    "El precio debe ser mayor a 0"
  )

  def mostrar(): Unit = {
    println(
      f"ID: ${id.getOrElse(0)}%-3d | Socio: ${socio.nombre} ${socio.apellido}%-20s | Código: ${socio.codigo}%-8s | Inicio: $fechaInicio | Fin: $fechaFin | Precio: S/ ${precio}%-8s | Estado: $estado"
    )
  }
}

object Membresia {

  def nueva(
             socio: Socio,
             fechaInicio: LocalDate,
             fechaFin: LocalDate,
             precio: BigDecimal,
             estado: EstadoSocio
           ): Membresia =
    Membresia(
      None,
      socio,
      fechaInicio,
      fechaFin,
      precio,
      estado
    )
}



