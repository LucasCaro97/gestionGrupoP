package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.CobroDetalleAdelanto;
import com.grupop.gestion.Entidades.PagoDetalleAdelanto;
import com.grupop.gestion.Repositorios.CobroDetalleAdelantoRepo;
import com.grupop.gestion.Repositorios.PagoDetalleAdelantoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PagoDetalleAdelantoServicio {

    private final PagoDetalleAdelantoRepo pagoDetalleAdelantoRepo;
    private final ProveedorServicio proveedorServicio;
    private final PagoServicio pagoServicio;

    @Transactional
    public void crear(Long idProv, Long idOperacion, BigDecimal importeAdelanto, String detalle) {
        PagoDetalleAdelanto p = new PagoDetalleAdelanto();
        p.setDetalle(detalle);
        p.setProveedor(proveedorServicio.buscarPorId(idProv));
        p.setPagoId(pagoServicio.obtenerPorId(idOperacion));
        p.setImporte(importeAdelanto);
        pagoDetalleAdelantoRepo.save(p);

        proveedorServicio.actualizarSaldoAFavor(idProv, importeAdelanto);
        pagoServicio.actualizarTotal(idOperacion);

    }

    @Transactional
    public void eliminarPorId(Long id) {
        PagoDetalleAdelanto p = pagoDetalleAdelantoRepo.findById(id).get();
        proveedorServicio.descontarSaldoAFavor(p.getProveedor().getId(), p.getImporte());
        pagoDetalleAdelantoRepo.deleteById(id);
        pagoServicio.actualizarTotal(p.getPagoId().getId());
    }

    @Transactional
    public List<PagoDetalleAdelanto> obtenerPorPago(Long id) {
        return pagoDetalleAdelantoRepo.obtenerPorPago(id);
    }

    @Transactional(readOnly = true)
    public PagoDetalleAdelanto obtenerPorId(Long idAdelanto) {
        return pagoDetalleAdelantoRepo.findById(idAdelanto).get();
    }
}
