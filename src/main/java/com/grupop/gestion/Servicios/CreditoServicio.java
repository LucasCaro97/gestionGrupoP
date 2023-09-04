package com.grupop.gestion.Servicios;

import com.grupop.gestion.DTO.CreditoDetalleDto;
import com.grupop.gestion.Entidades.*;
import com.grupop.gestion.Repositorios.CreditoRepo;
import jdk.swing.interop.SwingInterOpUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CreditoServicio {

    private final CreditoRepo creditoRepo;
    private final VentaServicio ventaServicio;
    private final PlanPagoServicio planPagoServicio;
    private final CreditoDetalleServicio creditoDetalleServicio;
    private final FormaDePagoDetalleSubDetalleServicio formaDePagoDetalleSubDetalleServicio;
    private final FormaDePagoDetalleServicio formaDePagoDetalleServicio;
    private final EmailService emailService;

    @Transactional
    public void crear(Credito dto, String venceLosDias){
        PlanPago plan = planPagoServicio.obtenerPorId(dto.getPlanPago().getId());

        Credito c = new Credito();
        c.setCliente(dto.getCliente());
        c.setFecha(dto.getFecha());
        c.setSector(dto.getSector());
        c.setTipoComprobante(dto.getTipoComprobante());
        c.setTalonario(dto.getTalonario());
        c.setNroComprobante(dto.getNroComprobante());
        c.setVenta(dto.getVenta());
        c.setPlanPago(dto.getPlanPago());
        c.setCantCuotas(dto.getCantCuotas());
        c.setPorcentajeInteres(dto.getPorcentajeInteres());
        c.setVencimiento(dto.getVencimiento());
        c.setInteresesTotales(dto.getCapital().multiply(dto.getPorcentajeInteres()).divide(new BigDecimal(100)));
        c.setGastosAdministrativos(dto.getCapital().multiply(plan.getPorcentajeGastos()).divide(new BigDecimal(100)));
        c.setCapital(dto.getCapital());
        c.setTotalCredito(c.getCapital().add(c.getInteresesTotales()).add(c.getGastosAdministrativos()));
        c.setObservaciones(dto.getObservaciones());
        c.setBloqueado(false);
        c.setDetallePago(formaDePagoDetalleSubDetalleServicio.obtenerPorIdOperacionAndIdTipoOperacion(c.getId(), 1l));
        creditoRepo.save(c);



        //GENERACION DE LA PRIMER FECHA DE VENCIMIENTO DEL CREDITO
        //DESESTRUCTURACION DE LA FECHA DE VENCIMIENTO POR DEFOULT
        LocalDate fechaActual = LocalDate.now();
        int anioActual = fechaActual.getYear();
        Month mesActual = fechaActual.getMonth();
        int diaVencimiento = Integer.parseInt(venceLosDias);

        //SI LA FECHA ACTUAL SE PASA DE LA FECHA DE VENC POR DEFOULT PASO AL SGTE MES
        if(fechaActual.getDayOfMonth() >= diaVencimiento){
            mesActual = mesActual.plus(1);
            if (mesActual == Month.JANUARY){
                anioActual = anioActual + 1;
            }
        }

        Credito credito = creditoRepo.findFirstByOrderByIdDesc();


        for(int i = 1; i <= credito.getCantCuotas() ; i++){

            LocalDate fechaVencimiento = LocalDate.of(anioActual,mesActual,diaVencimiento);
            creditoDetalleServicio.generarCuotas(credito, i, credito.getTotalCredito().divide(new BigDecimal(credito.getCantCuotas())), fechaVencimiento, credito.getCliente(), credito.getGastosAdministrativos().divide(new BigDecimal(credito.getCantCuotas()),4, RoundingMode.UP));


            if(mesActual == Month.DECEMBER){
                mesActual = mesActual.plus(1);
                anioActual++;
            }else{
                mesActual = mesActual.plus(1);
            }
        }

//        CIERRO LA VENTA PARA QUE NO SE PUEDA MODIFICAR NADA
        ventaServicio.cerrarVenta(dto.getVenta().getId());
//        CIERRO EL DETALLE DE PAGO DE LA VENTA
        formaDePagoDetalleServicio.cerrarDetallePago(dto.getVenta().getId(), dto.getVenta().getTipoOperacion().getId());
//    HAGO UN ENVIO DE EMAIL
        String email="lucascaro97@gmail.com";
        emailService.send(email, "credito", c.getVenta().getNroComprobante(), c.getCliente().getId(), c.getTotalCredito(), c.getPlanPago());
    }

    @Transactional
    public void actualizar(Credito dto){
        Credito c = creditoRepo.findById(dto.getId()).get();

        if(c.isBloqueado()){
            c.setObservaciones(dto.getObservaciones());;
            creditoRepo.save(c);
        }else{

            PlanPago plan = planPagoServicio.obtenerPorId(dto.getPlanPago().getId());

            c.setCliente(dto.getCliente());
            c.setFecha(dto.getFecha());
            c.setSector(dto.getSector());
            c.setTipoComprobante(dto.getTipoComprobante());
            c.setTalonario(dto.getTalonario());
            c.setNroComprobante(dto.getNroComprobante());
            c.setVenta(dto.getVenta());
            c.setCapital(dto.getCapital());
            c.setPlanPago(dto.getPlanPago());
            c.setCantCuotas(dto.getCantCuotas());
            c.setPorcentajeInteres(dto.getPorcentajeInteres());
            c.setVencimiento(dto.getVencimiento());
            c.setInteresesTotales(c.getCapital().multiply(c.getPorcentajeInteres()).divide(new BigDecimal(100)));
            c.setGastosAdministrativos(dto.getCapital().multiply(plan.getPorcentajeGastos()).divide(new BigDecimal(100)));
            c.setTotalCredito(c.getCapital().add(c.getInteresesTotales()).add(c.getGastosAdministrativos()));
            c.setObservaciones(dto.getObservaciones());
            creditoRepo.save(c);

        }
    }

    @Transactional(readOnly = true)
    public List<Credito> obtenerTodos(){ return creditoRepo.findAll(); }

    @Transactional(readOnly = true)
    public Credito obtenerPorId(Long id){ return creditoRepo.findById(id).get(); }

    @Transactional
    public void eliminarPorId(Long id){ creditoRepo.deleteById(id);}

    //AGREGAR VALIDACION - EXISTE UN CREDITO VINCULADO A ESTA VENTA ?
    @Transactional(readOnly = true)
    public Integer validarExistenciaPorVenta(Long idVenta){
        return creditoRepo.existByIdVenta(idVenta);
    }


    @Transactional(readOnly = true)
    public Long buscarUltimoId() {
        return creditoRepo.buscarUltimoId();
    }

    public void regenerarCuotas(Long idCredito,  List<CreditoDetalleDto> arrayListB){
        Credito c = creditoRepo.findById(idCredito).get();
        List<CreditoDetalle> arrayListA = creditoDetalleServicio.obtenerLineasDetalle(c.getId());

        System.out.println("***Iniciando comparacion***");
        boolean montoHaCambiado = false;
        boolean fechaHaCambiado = false;
        LocalDate nuevaFecha = LocalDate.now();
        BigDecimal acumulado = BigDecimal.ZERO;
        Integer contador = 0;
        BigDecimal montoCuotasRestantes = BigDecimal.ZERO;

        for(int i = 0; i < arrayListA.size(); i++){
            BigDecimal montoA = arrayListA.get(i).getMonto();
            BigDecimal montoB = arrayListB.get(i).getMonto();
            LocalDate fechaA = arrayListA.get(i).getVencimiento();
            LocalDate fechaB = arrayListB.get(i).getVencimiento();


            if(!montoA.equals(montoB)){
                montoHaCambiado = true;
                arrayListA.get(i).setMonto(montoB);
                arrayListA.get(i).setCapital(montoB.subtract(arrayListA.get(i).getGastoAdm()));
                arrayListA.get(i).setSaldo(montoB);

                acumulado = acumulado.add(montoB);
                contador++;
            }

            if(!fechaA.equals(fechaB)){
                fechaHaCambiado = true;
                nuevaFecha = fechaB;
            }
        }

        if(montoHaCambiado){
            System.out.println("Total cuotas nuevas: " + acumulado);
            System.out.println("Cuotas afectadas: " + contador);
            System.out.println("Cuotas a recalcular: " +  ( c.getCantCuotas() - contador ));
            System.out.println("Diferencia a ajustar: " + c.getTotalCredito().subtract(acumulado));
            montoCuotasRestantes = c.getTotalCredito().subtract(acumulado).divide( new BigDecimal ( c.getCantCuotas() - contador ), 2, RoundingMode.HALF_UP);
            System.out.println("Monto cuotas restantes: " + (montoCuotasRestantes));
            System.out.println("Total credito: " + c.getTotalCredito());

            System.out.println("---Recalculando cuotas restantes--- Credito: " + c.getId());
            for(int i = 0; i < arrayListA.size(); i++) {
                Integer nroCuota = arrayListA.get(i).getNroCuota();
                if(nroCuota > contador){

                    arrayListA.get(i).setMonto(montoCuotasRestantes);
                    arrayListA.get(i).setCapital(montoCuotasRestantes.subtract(arrayListA.get(i).getGastoAdm()));
                    arrayListA.get(i).setSaldo(montoCuotasRestantes);
                }
            }

            creditoDetalleServicio.actualizarCuotas(arrayListA);

        }else{
            System.out.println("No es necesario recalcular cuotas ya que ninguna ha cambiado");
        }

        if(fechaHaCambiado){
            System.out.println("---Recalculando fechas vencimiento--- Credito: " + c.getId());
            for(int i = 0; i < arrayListA.size(); i++) {
                System.out.println("Nro Cuota: fechaAnt " + arrayListA.get(i).getNroCuota() + " fecha Nueva: " + nuevaFecha);
                arrayListA.get(i).setVencimiento(nuevaFecha);
                nuevaFecha = nuevaFecha.plusMonths(1);
            }

            creditoDetalleServicio.actualizarCuotasFechas(arrayListA);

        }else{
            System.out.println("No es necesario recalcular fechas ya que ninguna ha cambiado");
        }




    }

    @Transactional(readOnly = true)
    public boolean validarEstado(Long idCred) { return creditoRepo.validarEstado(idCred);    }

    @Transactional
    public void marcarComoBloqueado(Long idCred) {
        Credito c = creditoRepo.findById(idCred).get();
        c.setBloqueado(true);
        creditoRepo.save(c);
    }
}