package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.TipoEmpleado;
import com.grupop.gestion.Entidades.TipoIva;
import com.grupop.gestion.Repositorios.TipoEmpleadoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoEmpleadoServicio {

    private final TipoEmpleadoRepo tipoEmpleadoRepo;

    @Transactional
    public void crear(TipoEmpleado dto){
        TipoEmpleado tipoEmpleado = new TipoEmpleado();
        tipoEmpleado.setDescripcion(dto.getDescripcion());
        tipoEmpleadoRepo.save(tipoEmpleado);
    }

    @Transactional
    public void actualizar(TipoEmpleado dto){
        TipoEmpleado tipoEmpleado = new TipoEmpleado();
        tipoEmpleado.setDescripcion(dto.getDescripcion());
        tipoEmpleadoRepo.save(tipoEmpleado);
    }
    @Transactional(readOnly = true)
    public List<TipoEmpleado> obtenerTodos(){
        return tipoEmpleadoRepo.findAll();
    }

    @Transactional
    public void eliminarPorId(Long id){ tipoEmpleadoRepo.deleteById(id);}

    @Transactional(readOnly = true)
    public TipoEmpleado buscarPorId(Long id){ return tipoEmpleadoRepo.findById(id).get(); }

}
