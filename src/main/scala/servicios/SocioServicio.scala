package servicios

import Enums.TipoDocumento
import modelo.Socio
import repositorios.SocioRepositorios

// Consultas sobre socios.
class SocioServicio(repo: SocioRepositorios = new SocioRepositorios()) {

  // FILTER: socios cuyo nombre, apellido o código contiene el texto buscado
  def buscarPorTexto(texto: String): List[Socio] = {
    val t = texto.trim.toLowerCase
    repo.listar().filter(s =>
      s.nombre.toLowerCase.contains(t) ||
        s.apellido.toLowerCase.contains(t) ||
        s.codigo.toLowerCase.contains(t)
    )
  }

  // FILTER: socios que usan un tipo de documento (Dni o CarnetExtranjeria)
  def porTipoDocumento(tipo: TipoDocumento): List[Socio] =
    repo.listar().filter(_.tipoDocumento == tipo)

  // FILTER: socios registrados en un año y mes determinados
  def registradosEnMes(anio: Int, mes: Int): List[Socio] =
    repo.listar().filter(s =>
      s.fechaRegistro.getYear == anio && s.fechaRegistro.getMonthValue == mes
    )

  // GROUPBY + MAP: cuántos socios hay por cada tipo de documento
  def contarPorTipoDocumento(): Map[TipoDocumento, Int] =
    repo.listar()
      .groupBy(_.tipoDocumento)
      .map { case (tipo, lista) => tipo -> lista.size }

  // FILTER: socios cuyo correo termina en un dominio (ej. "gmail.com")
  def porDominioCorreo(dominio: String): List[Socio] =
    repo.listar().filter(_.email.toLowerCase.endsWith("@" + dominio.toLowerCase))

  // FIND: devuelve Some(socio) si existe el código, o None si no existe
  def buscarPorCodigo(codigo: String): Option[Socio] =
    repo.listar().find(_.codigo.equalsIgnoreCase(codigo))

  // SORTBY: socios ordenados alfabéticamente por apellido y luego por nombre
  def ordenadosPorApellido(): List[Socio] =
    repo.listar().sortBy(s => (s.apellido.toLowerCase, s.nombre.toLowerCase))
}