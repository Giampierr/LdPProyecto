package servicios

import Enums.EstadoSocio
import modelo.{Membresias, Socio}
import repositorios.{MembresiasRepositorio, SocioRepositorios}

import java.time.LocalDate
import java.time.temporal.ChronoUnit

// Consultas sobre membresías.
class MembresiaServicio(
                         repo: MembresiasRepositorio = new MembresiasRepositorio(),
                         socioRepo: SocioRepositorios = new SocioRepositorios()
                       ) {

  // Una membresía está vigente si su estado es Activo y hoy está entre fechaInicio y fechaFin
  private def estaVigente(m: Membresias, hoy: LocalDate): Boolean =
    m.estado == EstadoSocio.Activo &&
      !hoy.isBefore(m.fechaInicio) &&
      !hoy.isAfter(m.fechaFin)

  // FILTER: membresías vigentes a la fecha indicada (por defecto, hoy)
  def vigentes(hoy: LocalDate = LocalDate.now()): List[Membresias] =
    repo.listar().filter(m => estaVigente(m, hoy))

  // FILTER: membresías cuya fecha de fin ya pasó
  def vencidas(hoy: LocalDate = LocalDate.now()): List[Membresias] =
    repo.listar().filter(_.fechaFin.isBefore(hoy))

  // FILTER + SORTBY: vigentes que vencen dentro de los próximos N días, la más próxima primero
  def porVencer(dias: Int, hoy: LocalDate = LocalDate.now()): List[Membresias] =
    vigentes(hoy)
      .filter(m => !m.fechaFin.isAfter(hoy.plusDays(dias)))
      .sortBy(_.fechaFin.toEpochDay)

  // FILTER: todas las membresías de un socio (por su id)
  def deSocio(idSocio: Int): List[Membresias] =
    repo.listar().filter(_.socio.id.contains(idSocio))

  // MAP + SUM: suma de los precios de todas las membresías
  def ingresosTotales(): BigDecimal =
    repo.listar().map(_.precio).sum

  // Promedio de precio (0 si no hay membresías, para evitar dividir entre cero)
  def precioPromedio(): BigDecimal = {
    val precios = repo.listar().map(_.precio)
    if (precios.isEmpty) BigDecimal(0) else precios.sum / BigDecimal(precios.size)
  }

  // MAXBYOPTION: membresía de mayor precio (Option porque la lista puede estar vacía)
  def masCara(): Option[Membresias] =
    repo.listar().maxByOption(_.precio)

  // MAP + DISTINCTBY: socios que tienen al menos una membresía vigente, sin repetir
  def sociosConMembresiaVigente(hoy: LocalDate = LocalDate.now()): List[Socio] =
    vigentes(hoy).map(_.socio).distinctBy(_.id)

  // FLATMAP + TOSET + FILTER: socios registrados que NO tienen membresía vigente
  def sociosSinMembresiaVigente(hoy: LocalDate = LocalDate.now()): List[Socio] = {
    val idsConMembresia = vigentes(hoy).flatMap(_.socio.id).toSet
    socioRepo.listar().filter(s => !s.id.exists(idsConMembresia.contains))
  }

  // MAP: nombre del socio y duración de su membresía en días
  def duracionesEnDias(): List[(String, Long)] =
    repo.listar().map(m =>
      (s"${m.socio.nombre} ${m.socio.apellido}",
        ChronoUnit.DAYS.between(m.fechaInicio, m.fechaFin))
    )

  // GROUPBY + MAP: cuántas membresías hay por estado
  def contarPorEstado(): Map[EstadoSocio, Int] =
    repo.listar()
      .groupBy(_.estado)
      .map { case (estado, lista) => estado -> lista.size }
}