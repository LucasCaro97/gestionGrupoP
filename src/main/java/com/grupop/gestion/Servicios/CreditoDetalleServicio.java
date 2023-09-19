package com.grupop.gestion.Servicios;

import com.grupop.gestion.DTO.CreditoDetalleDto;
import com.grupop.gestion.Entidades.Cliente;
import com.grupop.gestion.Entidades.Credito;
import com.grupop.gestion.Entidades.CreditoDetalle;
import com.grupop.gestion.Entidades.EstadoCredito;
import com.grupop.gestion.Repositorios.CreditoDetalleRepo;
import jakarta.servlet.ServletOutputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CreditoDetalleServicio {


    private final CreditoDetalleRepo creditoDetalleRepo;
    private final EstadoCreditoServicio estadoCreditoServicio;
    private final VentaServicio ventaServicio;
    private final IndiceCacServicio indiceCacServicio;

    @Transactional
    public void generarCuotas(Credito credito, int nroCuota, BigDecimal valorCuota, LocalDate fechaVencimiento, Cliente idCliente, BigDecimal gastoAdministrativo, EstadoCredito estadoCredito) {


        CreditoDetalle creditoDetalle = new CreditoDetalle();
        creditoDetalle.setCreditoId(credito);
        creditoDetalle.setNroCuota(nroCuota);
        creditoDetalle.setCapital(valorCuota.subtract(gastoAdministrativo));
        creditoDetalle.setGastoAdm(gastoAdministrativo);
        creditoDetalle.setMonto(valorCuota);
        creditoDetalle.setVencimiento(fechaVencimiento);
        creditoDetalle.setCliente(idCliente);
        creditoDetalle.setSaldo(valorCuota);
        creditoDetalle.setEstadoCuota(estadoCredito);
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
            c.setEstadoCuota(estadoCreditoServicio.obtenerPorId(2l));
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
        c.setEstadoCuota(estadoCreditoServicio.obtenerPorId(1l));
        creditoDetalleRepo.save(c);
    }

    @Transactional(readOnly = true)
    public List<CreditoDetalle> obtenerCreditosPendientesPorFkCliente(Long id){
        List<CreditoDetalle> listaCuotas = creditoDetalleRepo.obtenerPorFkClienteAndEstadoActivo(id);
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


    @Transactional(readOnly = true)
    public List<CreditoDetalle> obtenerCuotasCobrarMensual() {
        return creditoDetalleRepo.obtenerCuotasCobrarMensual();
    }

    @Transactional(readOnly = true)
    public LocalDate obtenerFechaPrimerVencimiento(Long idCredito){
        return creditoDetalleRepo.obtenerFechaPrimerVencimiento(idCredito);
    }

    @Transactional
    public void actualizarEstadoCuotasConSaldo(Credito dto) throws Exception {

        List<CreditoDetalle> listaCuotas = creditoDetalleRepo.obtenerPorCreditoId(dto.getId());
        

        switch (dto.getEstadoCredito().getId().intValue()){
            case 1:
                throw new Exception("No se puede volver al estado activo");
            case 2:
                throw new Exception("No se puede cancelar el credito desde este modulo");
            case 3:
                BigDecimal totalAtrasado = BigDecimal.ZERO;
                BigDecimal totalPendiente = BigDecimal.ZERO;
                BigDecimal totalRefinancia;
                BigDecimal indiceCacBase = BigDecimal.ZERO;
                BigDecimal indiceCacActual = BigDecimal.ZERO;
                BigDecimal ajuste = BigDecimal.ZERO;
                BigDecimal interesPun = BigDecimal.ZERO;

                LocalDate fechaPrimerVencimiento = listaCuotas.get(0).getVencimiento();


                // SI EL PLAN DE PAGO ES CON INDICE CAC OBTENGO EL INDICE BASE Y ACTUAL
                if(dto.getPlanPago().getTablaCac()) { // 1ro obtengo el indice estipulado en la venta
                    indiceCacBase = ventaServicio.obtenerIndiceBase(dto.getVenta().getId());
                    //BUSCO LOS INDICES
                    if(indiceCacBase.compareTo(BigDecimal.ZERO) == 0){

                        //OBTENGO EL INDICE BASE
                        do{
                            Integer indiceBuscar = 2;
                            indiceCacBase = indiceCacServicio.obtenerIndiceBase(fechaPrimerVencimiento.getMonthValue() - indiceBuscar, fechaPrimerVencimiento.getYear());
                            indiceBuscar++;
                        }while( indiceCacBase.compareTo(BigDecimal.ZERO) == 0);


                        //OBTENGO EL INDICE ACTUAL
                        do{
                            Integer indiceBuscar = 2;
                            indiceCacActual = indiceCacServicio.obtenerIndiceBase(LocalDate.now().getMonthValue() - indiceBuscar, LocalDate.now().getYear());
                            indiceBuscar++;
                        }while( indiceCacActual == BigDecimal.ZERO);

                    }
                }
                //RECORRO LAS CUOTAS DEL CREDITO
                for (CreditoDetalle c: listaCuotas) {
                    //SI ESTA CANCELADA LA LINEA NO HACE NADA
                    if (c.getSaldo().compareTo(BigDecimal.ZERO) == 0) { // LA CUOTA ESTA SALDADA - NO SE HACE NADA
                        System.out.println("Cuota nro " + c.getNroCuota() + " saldo " + c.getSaldo() + " - SALDADA");
                    } else {
                        //ACTUALIZO LA CUOTA CON ESTADO *REFINANCIADO*
                        c.setEstadoCuota(estadoCreditoServicio.obtenerPorId(3l));
                        creditoDetalleRepo.save(c);

                        // ***CALCULO EL MONTO A REFINANCIAR***
                        if (c.getVencimiento().getMonthValue() <= LocalDate.now().getMonthValue() && c.getVencimiento().getYear() <= LocalDate.now().getYear()) {
                            //***VERIFICO SI ESTA ATRASADO O SI ES LA CUOTA ACTUAL ( SE MANTIENE EL PUNITORIO Y EL AJUSTE )***
                            //CALCULAR AJUSTE CAC  -  CALCULAR PUNITORIO
                            if( dto.getPlanPago().getTablaCac() ){ // SI ES INDICE CAC CALCULO EL AJUSTE
                                BigDecimal ajustePlusCuotaBase = c.getMonto().multiply(indiceCacActual.divide(indiceCacBase, 16, RoundingMode.UP)).setScale(2, RoundingMode.UP);
                                ajuste = ajustePlusCuotaBase.subtract(c.getMonto());
                            }
                            // CALCULO LA DIFERENCIA DE DIAS SINO QUEDA EN 0
                            Long diasVencidos = ChronoUnit.DAYS.between(c.getVencimiento(), LocalDate.now());
                            //SI ESTA VENCIDO CALCULO EL INTERES PUNITORIO SINO QUEDA EN 0
                            if(diasVencidos > 0){
                                BigDecimal cuotaBasePlusAjuste = c.getSaldo().add(ajuste);
                                BigDecimal porPorcentaje = cuotaBasePlusAjuste.multiply(c.getCreditoId().getPlanPago().getInteresPunitorio());
                                BigDecimal porCantDias = porPorcentaje.multiply(new BigDecimal( diasVencidos));
                                BigDecimal totalLinea = porCantDias.divide(new BigDecimal(100), 2 , RoundingMode.DOWN);

                                interesPun = totalLinea;
                            }else{
                                interesPun = BigDecimal.ZERO;
                            }

                            totalAtrasado = totalAtrasado.add(c.getMonto().add(ajuste).add(interesPun));
                        }else{
                            //***CUOTAS PROXIMAS DEL MES ACTUAL HACIA ADELANTE***
                            //CALCULAR SALDO/MONTO MENOS GASTO ADMINISTRATIVO
                            if( dto.getPlanPago().getTablaCac() ) {
                                BigDecimal ajustePlusCuotaBase = c.getMonto().multiply(indiceCacActual.divide(indiceCacBase, 16, RoundingMode.UP)).setScale(2, RoundingMode.UP);
                                ajuste = ajustePlusCuotaBase.subtract(c.getMonto());
                            }
                            totalPendiente = totalPendiente.add( (c.getMonto().add(ajuste) ));
                        }
                    }
                }

                totalRefinancia = totalAtrasado.add( totalPendiente.divide(new BigDecimal(1.05) , 2 , RoundingMode.UP));
                creditoDetalleRepo.guardarSaldoRefinancia(totalRefinancia, dto.getId());
                break;
            case 4:
                for (CreditoDetalle c: listaCuotas) {
                    if(c.getMonto().compareTo(c.getSaldo()) != 0){
                        throw new Exception("No se puede anular un credito con cuotas cobradas");
                    }else{
                        c.setEstadoCuota(estadoCreditoServicio.obtenerPorId(4l));
                        creditoDetalleRepo.save(c);
                    }
                }
                break;
            case 5:
                for (CreditoDetalle c: listaCuotas) {
                    if(c.getSaldo().compareTo(BigDecimal.ZERO) == 0){
                        System.out.println("Cuota nro " + c.getNroCuota() + " saldo " + c.getSaldo() + " - SALDADA");
                    }else{
                        c.setEstadoCuota(estadoCreditoServicio.obtenerPorId(5l));
                        creditoDetalleRepo.save(c);
                    }
                }
                break;
            default:
                throw new Exception("El estado no coincide con ningun valor");

        }

    }

    @Transactional(readOnly = true)
    public boolean verificarSiEstaCancelado(Long idCredito) {
        if(creditoDetalleRepo.verificarSaldoByIdCredito(idCredito).compareTo(BigDecimal.ZERO) == 0){
            System.out.println("Saldo 0");
            return true;
        }else{
            System.out.println("Saldo " + creditoDetalleRepo.verificarSaldoByIdCredito(idCredito));
            return false;
        }

    }

    @Transactional(readOnly = true)
    public CreditoDetalle obtenerPorCreditoAndNroCuota(Long idCred, Integer nroCuota){
        return creditoDetalleRepo.obtenerPorIdCreditoAndNroCuota(idCred, nroCuota);

    }
}
