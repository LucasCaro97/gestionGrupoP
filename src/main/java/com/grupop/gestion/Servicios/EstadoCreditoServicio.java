package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.EstadoCredito;
import com.grupop.gestion.Repositorios.EstadoCreditoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstadoCreditoServicio {

    private final EstadoCreditoRepo estadoCreditoRepo;


    @Transactional
    public void crear(EstadoCredito dto){
        EstadoCredito e = new EstadoCredito();
        e.setDescripcion(dto.getDescripcion());
        estadoCreditoRepo.save(e);
    }

    @Transactional
    public void actualizar(EstadoCredito dto){
        EstadoCredito e = estadoCreditoRepo.findById(dto.getId()).get();
        e.setDescripcion(dto.getDescripcion());
        estadoCreditoRepo.save(e);
    }

    @Transactional(readOnly = true)
    public List<EstadoCredito> obtenerTodos(){ return estadoCreditoRepo.findAll();}

    @Transactional(readOnly = true)
    public EstadoCredito obtenerPorId(Long id){ return estadoCreditoRepo.findById(id).get(); }

    @Transactional
    public void eliminarPorId(Long id){ estadoCreditoRepo.deleteById(id);   }

}

