package com.grupop.gestion.Servicios;

import com.grupop.gestion.DTO.CobroDetalleCuotasDto;
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
        if ( cobroDetalleCuotasRepo.existByCreditoAndNroCuotaAndOperacion(idCred, nroCuota, idCobro) != 0) {
            CobroDetalleCuotas c = cobroDetalleCuotasRepo.searchByCreditoAndNroCuotaAndOperacion(idCred, nroCuota, idCobro);
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

    @Transactional
    public void crearNuevo(List<CobroDetalleCuotasDto> listaItems){
        for (CobroDetalleCuotasDto item: listaItems ) {
            if ( cobroDetalleCuotasRepo.existByCreditoAndNroCuotaAndOperacion(item.getIdCredito(), item.getNroCuota(), item.getIdCobro()) != 0) {
                CobroDetalleCuotas c = cobroDetalleCuotasRepo.searchByCreditoAndNroCuotaAndOperacion(item.getIdCredito(), item.getNroCuota(), item.getIdCobro());
                c.setImporteBonificacion(item.getImporteBonif());
                c.setImporteFinal(c.getImporteACobrar().subtract(c.getImporteBonificacion()));
                cobroDetalleCuotasRepo.save(c);
            }
            else {
                CreditoDetalle creditoCuota = creditoDetalleServicio.obtenerPorCreditoAndNroCuota(item.getIdCredito(), item.getNroCuota());

                CobroDetalleCuotas c = new CobroDetalleCuotas();
                c.setVentaId((ventaServicio.obtenerPorId(item.getIdVenta())));
                c.setCobroId(cobroServicio.obtenerPorId(item.getIdCobro()));
                c.setCreditoId(creditoServicio.obtenerPorId(item.getIdCredito()));
                c.setNroCuota(item.getNroCuota());
                c.setFechaVencimiento(creditoCuota.getVencimiento());
                c.setImporteCuota(item.getCuotaBase());
                c.setImporteAjuste(item.getAjuste());
                c.setImporteIntereses(item.getPunitorio());
                c.setImporteACobrar(item.getImporteCobrado());
                c.setImporteBonificacion(item.getImporteBonif());
                c.setImporteFinal(c.getImporteACobrar().subtract(c.getImporteBonificacion()));

                cobroDetalleCuotasRepo.save(c);

                creditoDetalleServicio.actualizarSaldo(item.getIdCredito(), item.getNroCuota(), c.getImporteACobrar());

                if(creditoDetalleServicio.verificarSiEstaCancelado(item.getIdCredito())){
                    creditoServicio.alterarEstado(item.getIdCredito(), 2l);
                    ventaServicio.alterarEstadoLotes(item.getIdVenta(), 3l);
                }

            }
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
        CobroDetalleCuotas c = cobroDetalleCuotasRepo.searchByCreditoAndNroCuotaAndOperacion(idCred, nroCuota, idCobro);

        if(c!=null){
            cobroDetalleCuotasRepo.eliminarDetalle(idCred, nroCuota, idCobro);
            creditoDetalleServicio.devolverSaldo(idCred, nroCuota, importeCobrado);
        }

    }


}
