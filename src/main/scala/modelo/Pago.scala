package modelo

import java.time.LocalDateTime
import scala.math.BigDecimal
case class Pago(
                 id: Option[Int],
                 fecha: LocalDateTime,
                 monto: BigDecimal
               ) {

  require(
    !fecha.isAfter(LocalDateTime.now()),
    "La fecha del pago no puede ser futura"
  )

  require(
    monto > 0,
    "El monto debe ser mayor a 0"
  )
}

object Pago {

  def nuevo(
             fecha: LocalDateTime,
             monto: BigDecimal
           ): Pago =
    Pago(
      None,
      fecha,
      monto
    )
}