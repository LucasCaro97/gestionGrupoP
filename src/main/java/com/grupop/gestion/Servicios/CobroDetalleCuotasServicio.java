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
import java.util.Collections;
import java.util.Comparator;
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
            c.setImporteBonificacion(importeBonif);
            c.setImporteFinal(c.getImporteACobrar().subtract(c.getImporteBonificacion()));
            cobroDetalleCuotasRepo.save(c);
//            cobroServicio.actualizarTotal(idCobro);
        }
        else {
            System.out.println("Creando cobro de cuota nro " + nroCuota);
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
//            cobroServicio.actualizarTotal(idCobro);

        }

    }

    @Transactional(readOnly = true)
    public List<CobroDetalleCuotas> obtenerPorCobro(Long id) {
        List<CobroDetalleCuotas> listaCuotas = cobroDetalleCuotasRepo.obtenerPorCobro(id);
        Collections.sort(listaCuotas, new Comparator<CobroDetalleCuotas>() {
            @Override
            public int compare(CobroDetalleCuotas o1, CobroDetalleCuotas o2) {
                return o1.getFechaVencimiento().compareTo(o2.getFechaVencimiento());
            }
        });

        return listaCuotas;
    }

    @Transactional
    public void eliminarLineaDetalle(Long idCred, Integer nroCuota, Long idCobro, BigDecimal importeCobrado){
        CobroDetalleCuotas c = cobroDetalleCuotasRepo.searchByCreditoAndNroCuota(idCred, nroCuota);

        if(c!=null){
            cobroDetalleCuotasRepo.eliminarDetalle(idCred, nroCuota, idCobro);
            creditoDetalleServicio.devolverSaldo(idCred, nroCuota, importeCobrado);
//            cobroServicio.actualizarTotal(idCobro);
        }

    }


}
