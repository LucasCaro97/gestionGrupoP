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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreditoDetalleServicio {


    private final CreditoDetalleRepo creditoDetalleRepo;

    @Transactional
    public void generarCuotas(Credito credito, int nroCuota, BigDecimal valorCuota, LocalDate fechaVencimiento, Cliente idCliente, BigDecimal gastoAdministrativo) {


        CreditoDetalle creditoDetalle = new CreditoDetalle();
        creditoDetalle.setCreditoId(credito);
        creditoDetalle.setNroCuota(nroCuota);
        creditoDetalle.setCapital(valorCuota.subtract(gastoAdministrativo));
        creditoDetalle.setGastoAdm(gastoAdministrativo);
        creditoDetalle.setMonto(valorCuota);
        creditoDetalle.setVencimiento(fechaVencimiento);
        creditoDetalle.setCliente(idCliente);
        creditoDetalle.setSaldo(valorCuota);
        creditoDetalle.setCobrado(false);
        creditoDetalleRepo.save(creditoDetalle);
    }

    @Transactional(readOnly = true)
    public List<CreditoDetalle> obtenerLineasDetalle(Long idCredito){
        return creditoDetalleRepo.obtenerPorCreditoId(idCredito);
    }

    @Transactional
    public void actualizarSaldo(Long creditoId, Integer nroCuota, BigDecimal importeCobrado){

        CreditoDetalle c = creditoDetalleRepo.buscarPorCreditoAndNroCuota(creditoId, nroCuota);

        if(c.getSaldo().subtract(importeCobrado).compareTo(BigDecimal.ZERO) <= 0){
            c.setSaldo(BigDecimal.ZERO);
            c.setCobrado(true);
        }else{
            c.setSaldo(c.getSaldo().subtract(importeCobrado));
        }
        creditoDetalleRepo.save(c);
    }

    @Transactional
    public void devolverSaldo(Long creditoId, Integer nroCuota, BigDecimal importeCobrado){
        CreditoDetalle c = creditoDetalleRepo.buscarPorCreditoAndNroCuota(creditoId, nroCuota);
        c.setSaldo(c.getSaldo().add(importeCobrado));
        c.setCobrado(false);
        creditoDetalleRepo.save(c);
    }

    @Transactional(readOnly = true)
    public List<CreditoDetalle> obtenerCreditosPendientesPorFkCliente(Long id){
        List<CreditoDetalle> listaCuotas = creditoDetalleRepo.obtenerPorFkClienteAndEstado(id);
        Collections.sort(listaCuotas, new Comparator<CreditoDetalle>() {
            @Override
            public int compare(CreditoDetalle o1, CreditoDetalle o2) {
                return o1.getVencimiento().compareTo(o2.getVencimiento());
            }
        });

        return listaCuotas;
        
    }

    @Transactional
    public void marcarComoNoCancelada(Long idCred, Integer nroCuota) {
        CreditoDetalle c = creditoDetalleRepo.buscarPorCreditoAndNroCuota(idCred, nroCuota);
        c.setCobrado(false);
        creditoDetalleRepo.save(c);
    }

    @Transactional(readOnly = true)
    public CreditoDetalle obtenerPorId(Long id){
        return creditoDetalleRepo.findById(id).get();
    }

    @Transactional
    public void actualizarCuotas(List<CreditoDetalle> arrayListA) {
        for (CreditoDetalle creditoDet : arrayListA) {
            Optional<CreditoDetalle> lineaCredito = creditoDetalleRepo.findById(creditoDet.getId());
            lineaCredito.get().setMonto(creditoDet.getMonto());
            creditoDetalleRepo.save(lineaCredito.get());
        }
    }

    @Transactional
    public void actualizarCuotasFechas(List<CreditoDetalle> arrayListA) {
        for (CreditoDetalle creditoDet : arrayListA){
            Optional<CreditoDetalle> lineaCredito = creditoDetalleRepo.findById(creditoDet.getId());
            lineaCredito.get().setVencimiento(creditoDet.getVencimiento());
            creditoDetalleRepo.save(lineaCredito.get());
        }
    }
}
