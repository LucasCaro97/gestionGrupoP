package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.CobroDetalleCuotas;
import com.grupop.gestion.Entidades.CreditoDetalle;
import com.grupop.gestion.Entidades.Venta;
import com.grupop.gestion.Entidades.VentaDetalle;
import com.grupop.gestion.Repositorios.CobroDetalleCuotasRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CobroDetalleCuotasServicio {

    private final CobroDetalleCuotasRepo cobroDetalleCuotasRepo;
    private final CobroServicio cobroServicio;
    private final VentaServicio ventaServicio;
    private final CreditoServicio creditoServicio;
    private final CreditoDetalleServicio creditoDetalleServicio;

    @Transactional
    public void crear(Long idVenta, Long idCred, Integer nroCuota, LocalDate fechaVenc, BigDecimal cuotaBase, BigDecimal ajuste,
                      BigDecimal importePuni, BigDecimal importeBonif, BigDecimal importeFinal, Long idCobro, BigDecimal cobrado) {
        if ( cobroDetalleCuotasRepo.existByCreditoAndNroCuota(idCred, nroCuota) != 0) {
            CobroDetalleCuotas c = cobroDetalleCuotasRepo.searchByCreditoAndNroCuota(idCred, nroCuota);
            c.setVentaId((ventaServicio.obtenerPorId(idVenta)));
            c.setCobroId(cobroServicio.obtenerPorId(idCobro));
            c.setCreditoId(creditoServicio.obtenerPorId(idCred));
            c.setNroCuota(nroCuota);
            c.setFechaVencimiento(fechaVenc);
            c.setImporteCuota(cuotaBase);
            c.setImporteAjuste(ajuste);
            c.setImporteIntereses(importePuni);
            c.setImporteACobrar(cobrado);
            c.setImporteBonificacion(importeBonif);
            c.setImporteFinal(c.getImporteACobrar().subtract(c.getImporteBonificacion()));
            cobroDetalleCuotasRepo.save(c);
        }
        else {
            CobroDetalleCuotas c = new CobroDetalleCuotas();
            c.setVentaId((ventaServicio.obtenerPorId(idVenta)));
            c.setCobroId(cobroServicio.obtenerPorId(idCobro));
            c.setCreditoId(creditoServicio.obtenerPorId(idCred));
            c.setNroCuota(nroCuota);
            c.setFechaVencimiento(fechaVenc);
            c.setImporteCuota(cuotaBase);
            c.setImporteAjuste(ajuste);
            c.setImporteIntereses(importePuni);
            c.setImporteACobrar(cobrado);
            c.setImporteBonificacion(importeBonif);
            c.setImporteFinal(c.getImporteACobrar().subtract(c.getImporteBonificacion()));
            cobroDetalleCuotasRepo.save(c);

            creditoDetalleServicio.actualizarSaldo(idCred, nroCuota, c.getImporteFinal());

        }

    }

    @Transactional(readOnly = true)
    public List<CobroDetalleCuotas> obtenerPorCobro(Long id) {
        return cobroDetalleCuotasRepo.obtenerPorCobro(id);
    }

    @Transactional
    public void eliminarLineaDetalle(Long idCred, Integer nroCuota, Long idCobro){
        cobroDetalleCuotasRepo.eliminarDetalle(idCred, nroCuota, idCobro);
        creditoDetalleServicio.marcarComoNoCancelada(idCred, nroCuota);

    }


}
