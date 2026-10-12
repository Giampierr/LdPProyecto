
package prolog

import scala.sys.process.*
import java.nio.file.Paths

object MotorReglas:

  private val rutaReglas =
    Paths.get("src/main/resources/reglas/gimnasio.pl")
      .toAbsolutePath
      .normalize()
      .toString

  private def ejecutar(argumentos: String*): String =
    val salida = new StringBuilder
    val errores = new StringBuilder

    val codigo = Process(
      Seq(
        """C:\Program Files\swipl\bin\swipl.exe""",
        "-q",
        "-s",
        rutaReglas
      ) ++ argumentos
    ).!(ProcessLogger(
      linea => salida.append(linea).append("\n"),
      linea => errores.append(linea).append("\n")
    ))

    if codigo != 0 then
      throw new RuntimeException(
        s"Error al ejecutar Prolog: ${errores.toString}"
      )

    salida.toString.trim

  private def consultarBooleano(
                                 predicado: String,
                                 idSocio: Int
                               ): Boolean =
    require(idSocio > 0, "El ID debe ser positivo")

    ejecutar(
      "-g", s"$predicado($idSocio)",
      "-t", "halt"
    ) match
      case "true"  => true
      case "false" => false
      case otro =>
        throw new RuntimeException(
          s"Respuesta inesperada de Prolog: $otro"
        )

  def puedeIngresar(idSocio: Int): Boolean =
    consultarBooleano("resultado_ingreso", idSocio)

  def necesitaRenovar(idSocio: Int): Boolean =
    consultarBooleano("resultado_renovacion", idSocio)

  def estaSuspendido(idSocio: Int): Boolean =
    consultarBooleano("resultado_suspension", idSocio)

  def listarSocios(): List[String] =
    val resultado = ejecutar(
      "-g", "mostrar_socios",
      "-t", "halt"
    )

    if resultado.isEmpty then List.empty
    else resultado.linesIterator.toList
