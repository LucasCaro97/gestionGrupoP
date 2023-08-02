package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.FormaDePago;
import com.grupop.gestion.Entidades.FormaDePagoDetalle;
import com.grupop.gestion.Entidades.FormaDePagoDetalleSubDetalle;
import com.grupop.gestion.Repositorios.FormaDePagoSubDetalleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FormaDePagoDetalleSubDetalleServicio {

    private final FormaDePagoSubDetalleRepo formaDePagoSubDetalleRepo;
    private final FormaDePagoServicio formaDePagoServicio;

    @Transactional
    public void crear(FormaDePagoDetalle formaDePagoDetalle, FormaDePago formaDePago, BigDecimal importe){
        FormaDePagoDetalleSubDetalle f = new FormaDePagoDetalleSubDetalle();
        f.setFormaDePagoDetalle(formaDePagoDetalle);
        f.setFormaPago(formaDePago);
        f.setMonto(importe);
        formaDePagoSubDetalleRepo.save(f);
    }

    @Transactional
    public void crearDetalleManual(FormaDePagoDetalleSubDetalle f){
        formaDePagoSubDetalleRepo.save(f);
    }

    @Transactional(readOnly = true)
    public FormaDePagoDetalleSubDetalle obtenerPorId(Long id){
        return formaDePagoSubDetalleRepo.findById(id).get();
    }

    @Transactional
    public void eliminarPorId(Long id){
        formaDePagoSubDetalleRepo.deleteById(id);
    }


    @Transactional(readOnly = true)
    public List<FormaDePagoDetalleSubDetalle> obtenerPorMaestro(Long idOperacion, Long idTipoOperacion) {
        return formaDePagoSubDetalleRepo.obtenerPorMaestro(idOperacion,idTipoOperacion);
    }

    public void eliminarPorIdOperacionAndTipoOperacion(Long idOperacion, Long tipoOperacion) {
        formaDePagoSubDetalleRepo.deleteByIdOperacionAndTipoOperacion(idOperacion,tipoOperacion);
    }
}
