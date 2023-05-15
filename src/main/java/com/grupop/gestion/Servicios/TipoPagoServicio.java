package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.TipoPago;
import com.grupop.gestion.Repositorios.TipoPagoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoPagoServicio {

    private final TipoPagoRepo tipoPagoRepo;

    @Transactional
    public void crear(TipoPago dto){
        TipoPago tipoPago = new TipoPago();
        tipoPago.setDescripcion(dto.getDescripcion());
        tipoPagoRepo.save(tipoPago);
    }

    @Transactional
    public void actualizar(TipoPago dto){
        TipoPago tipoPago = tipoPagoRepo.findById(dto.getId()).get();
        tipoPago.setDescripcion(dto.getDescripcion());
        tipoPagoRepo.save(tipoPago);
    }

    @Transactional(readOnly = true)
    public List<TipoPago> obtenerTodos(){
        return tipoPagoRepo.findAll();
    }
    @Transactional(readOnly = true)
    public TipoPago obtenerPorId(Long id) {      return tipoPagoRepo.findById(id).get() ; }
    @Transactional
    public void elimianrPorId(Long id){     tipoPagoRepo.deleteById(id); }
}
