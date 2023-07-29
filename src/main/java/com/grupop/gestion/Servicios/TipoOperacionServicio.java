package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.TipoOperacion;
import com.grupop.gestion.Repositorios.TipoOperacionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoOperacionServicio {

    private final TipoOperacionRepo tipoOperacionRepo;

    @Transactional
    public void crear(TipoOperacion dto){
        TipoOperacion tipoOperacion = new TipoOperacion();
        tipoOperacion.setDescripcion(dto.getDescripcion());
        tipoOperacionRepo.save(tipoOperacion);
    }

    @Transactional
    public void actualizar(TipoOperacion dto){
        TipoOperacion tipoOperacion = tipoOperacionRepo.findById(dto.getId()).get();
        tipoOperacion.setDescripcion(dto.getDescripcion());
        tipoOperacionRepo.save(tipoOperacion);
    }

    @Transactional(readOnly = true)
    public List<TipoOperacion> obtenerTodos(){  return tipoOperacionRepo.findAll();  }

    @Transactional(readOnly = true)
    public TipoOperacion obtenerPorId(Long id){ return tipoOperacionRepo.findById(id).get(); }

    @Transactional
    public void eliminarPorId(Long id){ tipoOperacionRepo.deleteById(id);}

}
