package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.TipoIva;
import com.grupop.gestion.Repositorios.TipoIvaRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoIvaServicio {

    private final TipoIvaRepo tipoIvaRepo;

    @Transactional
    public void crear(TipoIva dto){
        TipoIva tipoIva = new TipoIva();
        tipoIva.setDescripcion(dto.getDescripcion());
        tipoIvaRepo.save(dto);
    }

    @Transactional
    public void actualizar(TipoIva dto){
        TipoIva tipoIva = tipoIvaRepo.findById(dto.getId()).get();
        tipoIva.setDescripcion(dto.getDescripcion());
        tipoIvaRepo.save(dto);
    }

    @Transactional(readOnly = true)
    public List<TipoIva> obtenerTodos(){
        return tipoIvaRepo.findAll();
    }

    @Transactional
    public void eliminarPorId(Long id){
        tipoIvaRepo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public TipoIva buscarPorId(Long id){
        return tipoIvaRepo.findById(id).get();
    }


}
