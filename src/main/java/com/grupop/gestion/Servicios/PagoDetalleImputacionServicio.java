package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.*;
import com.grupop.gestion.Repositorios.PagoDetalleImputacionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PagoDetalleImputacionServicio {

    private final PagoDetalleImputacionRepo pagoDetalleImputacionRepo;
    private final PagoServicio pagoServicio;
    private final CuentasContablesServicio cuentasContablesServicio;

    @Transactional
    public void crear(Long idPago, Long idCuenta, BigDecimal importe) {
        Pago pago = pagoServicio.obtenerPorId(idPago);
        CuentasContables cuenta = cuentasContablesServicio.obtenerPorid(idCuenta);

        if (existByPagoAndCuenta(pago.getId(), cuenta.getId()) != 0) {
            PagoDetalleImputacion p = pagoDetalleImputacionRepo.searchByPagoAndCuenta(pago.getId(), cuenta.getId());
            p.setPagoId(pago);
            p.setCuenta(cuenta);
            p.setImporte(importe);
            pagoDetalleImputacionRepo.save(p);
            pagoServicio.actualizarTotal(idPago);
        } else {
            PagoDetalleImputacion p = new PagoDetalleImputacion();
            p.setPagoId(pago);
            p.setCuenta(cuenta);
            p.setImporte(importe);
            pagoDetalleImputacionRepo.save(p);
            pagoServicio.actualizarTotal(idPago);
        }

    }

    @Transactional(readOnly = true)
    public Integer existByPagoAndCuenta(Long idPago, Long idCuenta) {
        return pagoDetalleImputacionRepo.existsByPagoAndCuenta(idPago, idCuenta);
    }

    @Transactional(readOnly = true)
    public List<PagoDetalleImputacion> obtenerPorPago(Long idPago){
        return pagoDetalleImputacionRepo.obtenerPorPago(idPago);
    }

    @Transactional
    public void eliminarPorPagoAndCuenta(Long idPago,Long idCuenta){
        PagoDetalleImputacion p = pagoDetalleImputacionRepo.searchByPagoAndCuenta(idPago,idCuenta);
        pagoDetalleImputacionRepo.deleteById(p.getId());
        pagoServicio.actualizarTotal(idPago);
    }

}
