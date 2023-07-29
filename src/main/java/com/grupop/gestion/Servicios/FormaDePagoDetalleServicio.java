package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.FormaDePago;
import com.grupop.gestion.Entidades.FormaDePagoDetalle;
import com.grupop.gestion.Repositorios.FormaDePagoADetallarRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class FormaDePagoDetalleServicio {

    private final FormaDePagoADetallarRepo formaDePagoADetallarRepo;
    private final FormaDePagoDetalleSubDetalleServicio formaDePagoDetalleSubDetalleServicio;

    @Transactional
    //Crear Detalle de Pago automatico
    public void crear(Long idOperacion, Long idTipoOperacion, BigDecimal importe, FormaDePago formaDePago){
        FormaDePagoDetalle f = new FormaDePagoDetalle();
        f.setIdOperacion(idOperacion);
        f.setTipoOperacion(idTipoOperacion);
        f.setTotalOperacion(importe);
        formaDePagoADetallarRepo.save(f);

        formaDePagoDetalleSubDetalleServicio.crear(f, formaDePago, importe);
        System.out.println("Creado SubDetalle de Pago : idOperacion: " + idOperacion + " tipoOp: " + idTipoOperacion + " TOT: " + importe);


    }

    @Transactional(readOnly = true)
    public FormaDePagoDetalle obtenerPorId(Long idOperacion, Long tipoOperacion){
        return formaDePagoADetallarRepo.findByIdOperacionAndTipoOperacion(idOperacion, tipoOperacion);
    }




}
