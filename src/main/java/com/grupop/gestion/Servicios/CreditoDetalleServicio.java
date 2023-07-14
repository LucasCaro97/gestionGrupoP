package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.Cliente;
import com.grupop.gestion.Entidades.Credito;
import com.grupop.gestion.Entidades.CreditoDetalle;
import com.grupop.gestion.Repositorios.CreditoDetalleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreditoDetalleServicio {


    private final CreditoDetalleRepo creditoDetalleRepo;

    @Transactional
    public void generarCuotas(Credito credito, int nroCuota, BigDecimal valorCuota, LocalDate fechaVencimiento, Cliente idCliente) {

        CreditoDetalle creditoDetalle = new CreditoDetalle();
        creditoDetalle.setCreditoId(credito);
        creditoDetalle.setNroCuota(nroCuota);
        creditoDetalle.setMonto(valorCuota);
        creditoDetalle.setVencimiento(fechaVencimiento);
        creditoDetalle.setCliente(idCliente);
        creditoDetalle.setCobrado(false);
        creditoDetalleRepo.save(creditoDetalle);
    }

    @Transactional(readOnly = true)
    public List<CreditoDetalle> obtenerLineasDetalle(Long idCredito){
        return creditoDetalleRepo.obtenerPorCreditoId(idCredito);
    }

    @Transactional
    public void marcarComoCancelada(Long creditoId, Integer nroCuota){
        CreditoDetalle c = creditoDetalleRepo.buscarPorCreditoAndNroCuota(creditoId, nroCuota);
        c.setCobrado(true);
        creditoDetalleRepo.save(c);
    }

    @Transactional(readOnly = true)
    public List<CreditoDetalle> obtenerCreditosPendientesPorFkCliente(Long id){
        return creditoDetalleRepo.obtenerPorFkClienteAndEstado(id);
        
    }

    @Transactional
    public void marcarComoNoCancelada(Long idCred, Integer nroCuota) {
        CreditoDetalle c = creditoDetalleRepo.buscarPorCreditoAndNroCuota(idCred, nroCuota);
        c.setCobrado(false);
        creditoDetalleRepo.save(c);
    }
}
