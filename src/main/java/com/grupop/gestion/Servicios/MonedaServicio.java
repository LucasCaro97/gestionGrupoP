package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.Moneda;
import com.grupop.gestion.Repositorios.MonedaRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MonedaServicio {

    public final MonedaRepo monedaRepo;

    @Transactional
    public void crear(Moneda dto){
        Moneda moneda = new Moneda();
        moneda.setDescripcion(dto.getDescripcion());
        moneda.setAbreviatura(dto.getAbreviatura());
        moneda.setSimbolo(dto.getSimbolo());
        moneda.setCotizacion(dto.getCotizacion());
        moneda.setCotizacionVta(dto.getCotizacionVta());
        monedaRepo.save(moneda);
    }

    @Transactional
    public void actualizar(Moneda dto){
        Moneda moneda = monedaRepo.findById(dto.getId()).get();
        moneda.setDescripcion(dto.getDescripcion());
        moneda.setAbreviatura(dto.getAbreviatura());
        moneda.setSimbolo(dto.getSimbolo());
        moneda.setCotizacion(dto.getCotizacion());
        moneda.setCotizacionVta(dto.getCotizacionVta());
        monedaRepo.save(moneda);
    }

    @Transactional(readOnly = true)
    public List<Moneda> obtenerTodos(){     return monedaRepo.findAll();    }

    @Transactional(readOnly = true)
    public Moneda obtenerPorId(Long id){    return monedaRepo.findById(id).get();    }

    @Transactional
    public void eliminarPorId(Long id){ monedaRepo.deleteById(id);  }

}
