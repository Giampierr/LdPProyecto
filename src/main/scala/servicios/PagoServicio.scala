package servicios

import modelo.Pago
import repositorios.PagosRepositorio

import java.time.YearMonth

// Consultas sobre pagos.0
class PagoServicio {

  // FILTER: pagos realizados en un año y mes determinados
  def delMes(anio: Int, mes: Int): List[Pago] =
    PagosRepositorio().listar().filter(p =>
      p.fecha.getYear == anio && p.fecha.getMonthValue == mes
    )

  // FILTER: pagos cuyo monto supera un valor
  def mayoresA(monto: BigDecimal): List[Pago] =
    PagosRepositorio().listar().filter(_.monto > monto)

  // MAP + SUM: total recaudado en todos los pagos
  def totalRecaudado(): BigDecimal =
    PagosRepositorio().listar().map(_.monto).sum

  // Promedio de los montos (0 si no hay pagos)
  def promedio(): BigDecimal = {
    val montos = PagosRepositorio().listar().map(_.monto)
    if (montos.isEmpty) BigDecimal(0) else montos.sum / BigDecimal(montos.size)
  }

  // MAXBYOPTION: el pago de mayor monto
  def mayorPago(): Option[Pago] =
    PagosRepositorio().listar().maxByOption(_.monto)

  // GROUPBY + MAP + SORTBY: total recaudado por cada mes, del más antiguo al más reciente
  def recaudadoPorMes(): List[(YearMonth, BigDecimal)] =
    PagosRepositorio().listar()
      .groupBy(p => YearMonth.from(p.fecha))
      .map { case (mes, pagos) => mes -> pagos.map(_.monto).sum }
      .toList
      .sortBy { case (mes, _) => mes.getYear * 100 + mes.getMonthValue }
}