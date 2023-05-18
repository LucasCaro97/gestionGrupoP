package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.Empleado;
import com.grupop.gestion.Repositorios.EmpleadoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmpleadoServicio {

    private final EmpleadoRepo empleadoRepo;


    @Transactional
    public void crear(){
        Empleado emp = new Empleado();
        empleadoRepo.save(emp);
    }

    @Transactional
    public void actualizar(Empleado dto){
        Empleado emp = empleadoRepo.findById(dto.getId()).get();
        emp.setTipoEmpleado(dto.getTipoEmpleado());
        emp.setPuesto(dto.getPuesto());
        emp.setSector(dto.getSector());
        emp.setTurno(dto.getTurno());
        emp.setDepartamento(dto.getDepartamento());
        empleadoRepo.save(emp);
    }

    @Transactional
    public void eliminar(Long id){
        empleadoRepo.deleteById(id);
    }


    @Transactional(readOnly = true)
    public Long buscarUltimoId(){ return empleadoRepo.findLastId(); }

    @Transactional(readOnly = true)
    public Empleado buscarPorId(Long id){ return empleadoRepo.findById(id).get(); }

    @Transactional(readOnly = true)
    public List<Empleado> obtenerTodos(){ return empleadoRepo.findEmployee(); }
}
