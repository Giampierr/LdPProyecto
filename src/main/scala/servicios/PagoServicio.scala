package servicios

import modelo.Pago
import repositorios.PagosRepositorio

import java.time.{LocalDate, YearMonth}

// Consultas sobre pagos.
class PagoServicio(repo: PagosRepositorio = new PagosRepositorio()) {

  // FILTER: pagos realizados en un año y mes determinados
  def delMes(anio: Int, mes: Int): List[Pago] =
    repo.listar().filter(p =>
      p.fecha.getYear == anio && p.fecha.getMonthValue == mes
    )

  // FILTER: pagos cuyo monto supera un valor
  def mayoresA(monto: BigDecimal): List[Pago] =
    repo.listar().filter(_.monto > monto)

  // MAP + SUM: total recaudado en todos los pagos
  def totalRecaudado(): BigDecimal =
    repo.listar().map(_.monto).sum

  // Promedio de los montos (0 si no hay pagos)
  def promedio(): BigDecimal = {
    val montos = repo.listar().map(_.monto)
    if (montos.isEmpty) BigDecimal(0) else montos.sum / BigDecimal(montos.size)
  }

  // MAXBYOPTION: el pago de mayor monto
  def mayorPago(): Option[Pago] =
    repo.listar().maxByOption(_.monto)

  // GROUPBY + MAP + SORTBY: total recaudado por cada mes, del más antiguo al más reciente
  def recaudadoPorMes(): List[(YearMonth, BigDecimal)] =
    repo.listar()
      .groupBy(p => YearMonth.from(p.fecha))
      .map { case (mes, pagos) => mes -> pagos.map(_.monto).sum }
      .toList
      .sortBy { case (mes, _) => mes.getYear * 100 + mes.getMonthValue }
}