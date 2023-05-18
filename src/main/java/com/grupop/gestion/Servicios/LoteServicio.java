package com.grupop.gestion.Servicios;


import com.grupop.gestion.Entidades.Lote;
import com.grupop.gestion.Repositorios.LoteRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoteServicio {

    private final LoteRepo loteRepo;

    @Transactional
    public void crear(Lote dto){
        Lote lote = new Lote();
        lote.setDescripcion(dto.getDescripcion());
        lote.setSuperficieM2(dto.getSuperficieM2());
        lote.setNroPartida(dto.getNroPartida());
        lote.setNroPlano(dto.getNroPlano());
        lote.setUrbanizacion(dto.getUrbanizacion());
        lote.setManzana(dto.getManzana());
        lote.setImpuestoProv(dto.getImpuestoProv());
        lote.setImpuestoMun(dto.getImpuestoMun());
        lote.setUbicacion(dto.getUbicacion());
        loteRepo.save(dto);
    }

    @Transactional
    public void actualizar(Lote dto){
        Lote lote = loteRepo.findById(dto.getId()).get();
        lote.setDescripcion(dto.getDescripcion());
        lote.setSuperficieM2(dto.getSuperficieM2());
        lote.setNroPartida(dto.getNroPartida());
        lote.setNroPlano(dto.getNroPlano());
        lote.setUrbanizacion(dto.getUrbanizacion());
        lote.setManzana(dto.getManzana());
        lote.setImpuestoProv(dto.getImpuestoProv());
        lote.setImpuestoMun(dto.getImpuestoMun());
        lote.setUbicacion(dto.getUbicacion());
        loteRepo.save(dto);
    }

    @Transactional(readOnly = true)
    public List<Lote> obtenerTodos(){ return loteRepo.findAll(); }

    @Transactional(readOnly = true)
    public Lote obtenerPorId(Long id){ return loteRepo.findById(id).get(); }

    @Transactional
    public void eliminarPorId(Long id){ loteRepo.deleteById(id);}

}
