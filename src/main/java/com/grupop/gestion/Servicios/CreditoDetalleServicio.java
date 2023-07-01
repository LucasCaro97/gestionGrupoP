package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.Credito;
import com.grupop.gestion.Entidades.CreditoDetalle;
import com.grupop.gestion.Repositorios.CreditoDetalleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CreditoDetalleServicio {


    private final CreditoDetalleRepo creditoDetalleRepo;

    public void generarCuotas(Credito credito, int nroCuota, BigDecimal valorCuota) {
        CreditoDetalle creditoDetalle = new CreditoDetalle();
        creditoDetalle.setCreditoId(credito);
        creditoDetalle.setNroCuota(nroCuota);
        creditoDetalle.setMonto(valorCuota);
        creditoDetalleRepo.save(creditoDetalle);
    }
}
