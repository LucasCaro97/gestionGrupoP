package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.CobroDetalleCuotas;
import com.grupop.gestion.Repositorios.CobroDetalleCuotasRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CobroDetalleCuotasServicio {

    private final CobroDetalleCuotasRepo cobroDetalleCuotasRepo;
    private final CobroServicio cobroServicio;
    private final VentaServicio ventaServicio;
    private final CreditoServicio creditoServicio;
    private final CreditoDetalleServicio creditoDetalleServicio;

    @Transactional
    public void crear(Long cobroId, Long ventaId, Long creditoId, Integer nroCuota, LocalDate fechaVencimiento,
                      BigDecimal importeCuota, BigDecimal importeACobrar, BigDecimal importeIntereses, BigDecimal importeAjuste, BigDecimal porcIndice){
        CobroDetalleCuotas c = new CobroDetalleCuotas();
        c.setCobroId(cobroServicio.obtenerPorId(cobroId));
        c.setVentaId(ventaServicio.obtenerPorId(ventaId));
        c.setCreditoId(creditoServicio.obtenerPorId(creditoId));
        c.setNroCuota(nroCuota);
        c.setFechaVencimiento(fechaVencimiento);
        c.setImporteCuota(importeCuota);
        c.setImporteACobrar(importeACobrar);
        c.setImporteIntereses(importeIntereses);
        c.setImporteAjuste(importeAjuste);
        c.setPorcIndice(porcIndice);
        cobroDetalleCuotasRepo.save(c);
        creditoDetalleServicio.marcarComoCancelada(creditoId, nroCuota);
    }


}
