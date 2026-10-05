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

  def mostrar(): Unit = {
    println(
      f"ID: ${id.getOrElse(0)}%-3d | Fecha: ${fecha.toLocalDate} ${fecha.toLocalTime.toString.take(8)}%-17s | Monto: S/ ${monto}%.2f"
    )
  }
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