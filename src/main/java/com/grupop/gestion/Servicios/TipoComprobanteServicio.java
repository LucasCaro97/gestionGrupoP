package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.TipoComprobante;
import com.grupop.gestion.Repositorios.TipoComprobanteRepo;
import jakarta.persistence.Id;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoComprobanteServicio {

    private final TipoComprobanteRepo tipoComprobanteRepo;

    @Transactional
    public void crear(TipoComprobante dto){
        TipoComprobante tc = new TipoComprobante();
        tc.setDescripcion(dto.getDescripcion());
        tc.setTipoOperacion(dto.getTipoOperacion());
        tipoComprobanteRepo.save(tc);
    }

    @Transactional
    public void actualizar(TipoComprobante dto){
        TipoComprobante tc = tipoComprobanteRepo.findById(dto.getId()).get();
        tc.setDescripcion(dto.getDescripcion());
        tc.setTipoOperacion(dto.getTipoOperacion());
        tipoComprobanteRepo.save(tc);
    }

    @Transactional(readOnly = true)
    public List<TipoComprobante> obtenerTodos(){ return tipoComprobanteRepo.findAll();    }

    @Transactional(readOnly = true)
    public TipoComprobante obtenerPorId(Long id){ return tipoComprobanteRepo.findById(id).get() ;}

    @Transactional
    public void eliminarPorId(Long id){ tipoComprobanteRepo.deleteById(id);}

    @Transactional(readOnly = true)
    public List<TipoComprobante> obtenerPorTipoDeOperacion(Long id){
        return tipoComprobanteRepo.searchByTipoComprobante(id);
    }
}
