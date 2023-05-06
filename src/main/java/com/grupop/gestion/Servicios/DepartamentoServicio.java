package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.Departamento;
import com.grupop.gestion.Entidades.Puesto;
import com.grupop.gestion.Repositorios.DepartamentoRepo;
import com.grupop.gestion.Repositorios.PuestoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartamentoServicio {

    private final DepartamentoRepo departamentoRepo;

    @Transactional
    public void crear(Departamento dto){
        Departamento departamento = new Departamento();
        departamento.setDescripcion(dto.getDescripcion());
        departamentoRepo.save(departamento);
    }

    @Transactional
    public void actualizar(Departamento dto){
        Departamento departamento = departamentoRepo.findById(dto.getId()).get();
        departamento.setDescripcion(dto.getDescripcion());
        departamentoRepo.save(departamento);
    }
    @Transactional(readOnly = true)
    public List<Departamento> obtenerTodos(){
        return departamentoRepo.findAll();
    }

    @Transactional
    public void eliminarPorId(Long id){ departamentoRepo.deleteById(id);}

    @Transactional(readOnly = true)
    public Departamento buscarPorId(Long id){ return departamentoRepo.findById(id).get(); }

}
