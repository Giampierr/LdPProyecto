package servicios

import Enums.EstadoSocio.ACTIVA
import modelo.Membresia
import repositorios.MembresiasRepositorio

class MembresiaReportes {
  def membresiasActivas() : List[Membresia] ={
    MembresiasRepositorio().listar()
      .filter(m => m.estado == ACTIVA)
  }
}
