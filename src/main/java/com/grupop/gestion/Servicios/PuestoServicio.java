package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.Puesto;
import com.grupop.gestion.Entidades.TipoEmpleado;
import com.grupop.gestion.Repositorios.PuestoRepo;
import com.grupop.gestion.Repositorios.TipoEmpleadoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PuestoServicio {

    private final PuestoRepo puestoRepo;
    @Transactional
    public void crear(Puesto dto){
        Puesto puesto = new Puesto();
        puesto.setDescripcion(dto.getDescripcion());
        puestoRepo.save(puesto);
    }

    @Transactional
    public void actualizar(Puesto dto){
        Puesto puesto = puestoRepo.findById(dto.getId()).get();
        puesto.setDescripcion(dto.getDescripcion());
        puestoRepo.save(puesto);
    }
    @Transactional(readOnly = true)
    public List<Puesto> obtenerTodos(){
        return puestoRepo.findAll();
    }

    @Transactional
    public void eliminarPorId(Long id){ puestoRepo.deleteById(id);}

    @Transactional(readOnly = true)
    public Puesto buscarPorId(Long id){ return puestoRepo.findById(id).get(); }

}