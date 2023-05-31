package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.EstadoLote;
import com.grupop.gestion.Repositorios.EstadoLoteRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstadoLoteServicio {

    private final EstadoLoteRepo estadoLoteRepo;

    @Transactional
    public void crear(EstadoLote dto){
        EstadoLote est = new EstadoLote();
        est.setDescripcion(dto.getDescripcion());
        estadoLoteRepo.save(est);
        }

    @Transactional
    public void actualizar(EstadoLote dto){
        EstadoLote est = estadoLoteRepo.findById(dto.getId()).get();
        est.setDescripcion(dto.getDescripcion());
        estadoLoteRepo.save(est);
        }

    @Transactional(readOnly = true)
    public List<EstadoLote> obtenerTodos(){ return estadoLoteRepo.findAll(); }

    @Transactional(readOnly = true)
    public EstadoLote buscarPorId(Long id) { return estadoLoteRepo.findById(id).get(); }

    @Transactional
    public void eliminarPorId(Long id) { estadoLoteRepo.deleteById(id);}

}
