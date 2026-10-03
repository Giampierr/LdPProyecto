package modelo

import java.time.LocalDate

import scala.math.BigDecimal

import Enums.EstadoSocio

case class Membresias(
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
}

object Membresias {

  def nueva(
             socio: Socio,
             fechaInicio: LocalDate,
             fechaFin: LocalDate,
             precio: BigDecimal,
             estado: EstadoSocio
           ): Membresias =
    Membresias(
      None,
      socio,
      fechaInicio,
      fechaFin,
      precio,
      estado
    )
}