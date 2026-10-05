import Enums.TipoDocumento
import servicios.{MembresiaReportes, PagoServicio, SocioServicio}

import scala.io.StdIn.readLine

object Main {

  private val membresiaReportes = new MembresiaReportes()
  private val pagoServicio = new PagoServicio()
  private val socioServicio = new SocioServicio()

  def main(args: Array[String]): Unit = {

    var continuar = true

    while (continuar) {

      println("\n======================================")
      println("       SISTEMA DE REPORTES GIMNASIO")
      println("======================================")
      println("1. Reportes de Membresías")
      println("2. Reportes de Pagos")
      println("3. Reportes de Socios")
      println("0. Salir")
      println("======================================")

      print("Seleccione una opción: ")

      readLine() match {

        case "1" =>
          menuMembresias()

        case "2" =>
          menuPagos()

        case "3" =>
          menuSocios()

        case "0" =>
          continuar = false
          println("Saliendo del sistema...")

        case _ =>
          println("Opción inválida.")
      }
    }
  }


  def menuMembresias(): Unit = {

    println("\n========== MEMBRESÍAS ==========")
    println("1. Mostrar membresías activas")
    println("0. Regresar")

    print("Seleccione una opción: ")

    readLine() match {

      case "1" =>
        val membresias = membresiaReportes.membresiasActivas()

        println("\n--- MEMBRESÍAS ACTIVAS ---")

        if (membresias.isEmpty) {
          println("No hay membresías activas.")
        } else {
          membresias.foreach(println)
        }

      case "0" =>
        return

      case _ =>
        println("Opción inválida.")
    }
  }

  def menuPagos(): Unit = {

    println("\n============== PAGOS ==============")
    println("1. Pagos de un mes")
    println("2. Pagos mayores a un monto")
    println("3. Total recaudado")
    println("4. Promedio de pagos")
    println("5. Pago de mayor monto")
    println("6. Recaudado por mes")
    println("0. Regresar")
    println("====================================")

    print("Seleccione una opción: ")

    readLine() match {

      case "1" =>
        print("Ingrese año: ")
        val anio = readLine().toInt

        print("Ingrese mes (1-12): ")
        val mes = readLine().toInt

        val pagos = pagoServicio.delMes(anio, mes)

        println(s"\n--- PAGOS DE $mes/$anio ---")

        if (pagos.isEmpty)
          println("No se encontraron pagos.")
        else
          pagos.foreach(println)

      case "2" =>
        print("Ingrese monto mínimo: ")
        val monto = BigDecimal(readLine())

        val pagos = pagoServicio.mayoresA(monto)

        println(s"\n--- PAGOS MAYORES A $monto ---")

        if (pagos.isEmpty)
          println("No se encontraron pagos.")
        else
          pagos.foreach(println)

      case "3" =>
        val total = pagoServicio.totalRecaudado()

        println("\n--- TOTAL RECAUDADO ---")
        println(s"S/ $total")

      case "4" =>
        val promedio = pagoServicio.promedio()

        println("\n--- PROMEDIO DE PAGOS ---")
        println(s"S/ $promedio")

      case "5" =>
        println("\n--- PAGO DE MAYOR MONTO ---")

        pagoServicio.mayorPago() match {
          case Some(pago) =>
            println(pago)

          case None =>
            println("No existen pagos registrados.")
        }

      case "6" =>
        val resultados = pagoServicio.recaudadoPorMes()

        println("\n--- RECAUDADO POR MES ---")

        if (resultados.isEmpty) {
          println("No existen pagos.")
        } else {
          resultados.foreach {
            case (mes, total) =>
              println(s"$mes -> S/ $total")
          }
        }

      case "0" =>
        return

      case _ =>
        println("Opción inválida.")
    }
  }

  // =====================================================
  // MENÚ SOCIOS
  // =====================================================

  def menuSocios(): Unit = {

    println("\n=============== SOCIOS ===============")
    println("1. Buscar por texto")
    println("2. Buscar por tipo de documento")
    println("3. Socios registrados en un mes")
    println("4. Contar socios por tipo de documento")
    println("5. Buscar por dominio de correo")
    println("6. Buscar por código")
    println("7. Ordenar por apellido y nombre")
    println("0. Regresar")
    println("=======================================")

    print("Seleccione una opción: ")

    readLine() match {

      case "1" =>
        print("Ingrese nombre, apellido o código: ")
        val texto = readLine()

        val socios = socioServicio.buscarPorTexto(texto)

        println("\n--- RESULTADOS ---")

        if (socios.isEmpty)
          println("No se encontraron socios.")
        else
          socios.foreach(println)

      case "2" =>
        println("\nTipo de documento:")
        println("1. DNI")
        println("2. Carnet de Extranjería")

        print("Seleccione: ")

        val tipo = readLine() match {
          case "1" => TipoDocumento.Dni
          case "2" => TipoDocumento.CarnetExtranjeria
          case _ =>
            println("Tipo inválido.")
            return
        }

        val socios = socioServicio.porTipoDocumento(tipo)

        println(s"\n--- SOCIOS CON DOCUMENTO $tipo ---")

        if (socios.isEmpty)
          println("No se encontraron socios.")
        else
          socios.foreach(println)

      case "3" =>
        print("Ingrese año: ")
        val anio = readLine().toInt

        print("Ingrese mes (1-12): ")
        val mes = readLine().toInt

        val socios = socioServicio.registradosEnMes(anio, mes)

        println(s"\n--- SOCIOS REGISTRADOS EN $mes/$anio ---")

        if (socios.isEmpty)
          println("No se encontraron socios.")
        else
          socios.foreach(println)

      case "4" =>
        val resultados = socioServicio.contarPorTipoDocumento()

        println("\n--- SOCIOS POR TIPO DE DOCUMENTO ---")

        resultados.foreach {
          case (tipo, cantidad) =>
            println(s"$tipo -> $cantidad socios")
        }

      case "5" =>
        print("Ingrese dominio (ej. gmail.com): ")
        val dominio = readLine()

        val socios = socioServicio.porDominioCorreo(dominio)

        println(s"\n--- SOCIOS CON @$dominio ---")

        if (socios.isEmpty)
          println("No se encontraron socios.")
        else
          socios.foreach(println)

      case "6" =>
        print("Ingrese código del socio: ")
        val codigo = readLine()

        println("\n--- RESULTADO ---")

        socioServicio.buscarPorCodigo(codigo) match {

          case Some(socio) =>
            println(socio)

          case None =>
            println("No existe un socio con ese código.")
        }

      case "7" =>
        val socios = socioServicio.ordenadosPorApellido()

        println("\n--- SOCIOS ORDENADOS ---")

        socios.foreach(println)

      case "0" =>
        return

      case _ =>
        println("Opción inválida.")
    }
  }
}