package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.Credito;
import com.grupop.gestion.Entidades.CreditoDetalle;
import com.grupop.gestion.Entidades.PlanPago;
import com.grupop.gestion.Repositorios.CreditoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreditoServicio {

    private final CreditoRepo creditoRepo;
    private final VentaServicio ventaServicio;
    private final PlanPagoServicio planPagoServicio;
    private final CreditoDetalleServicio creditoDetalleServicio;

    @Transactional
    public void crear(Credito dto){
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
        c.setInteresesTotales(dto.getCapital().multiply(dto.getPorcentajeInteres()).divide(new BigDecimal(100)));
        c.setGastosAdministrativos(dto.getCapital().multiply(plan.getGastosAdministrativos()).divide(new BigDecimal(100)));
        c.setCapital(dto.getCapital());
        c.setTotalCredito(c.getCapital().add(c.getInteresesTotales()).add(c.getGastosAdministrativos()));
        c.setObservaciones(dto.getObservaciones());
        creditoRepo.save(c);

        Credito credito = creditoRepo.findFirstByOrderByIdDesc();
        System.out.println(credito);

//        for(int i = 1; i <= plan.getCantCuota() ; i++){
//            creditoDetalleServicio.generarCuotas(credito, i, credito.getTotalCredito().multiply(new BigDecimal(credito.getCantCuotas())));
//        }


        //CIERRO LA VENTA PARA QUE NO SE PUEDA MODIFICAR NADA
        System.out.println("cerrando venta: " + dto.getVenta().getId());
        ventaServicio.cerrarVenta(dto.getVenta().getId());

    }

    @Transactional
    public void actualizar(Credito dto){
        PlanPago plan = planPagoServicio.obtenerPorId(dto.getPlanPago().getId());

        Credito c = creditoRepo.findById(dto.getId()).get();
        c.setCliente(dto.getCliente());
        c.setFecha(dto.getFecha());
        c.setSector(dto.getSector());
        c.setTipoComprobante(dto.getTipoComprobante());
        c.setTalonario(dto.getTalonario());
        c.setNroComprobante(dto.getNroComprobante());
        c.setVenta(dto.getVenta());
        c.setCapital(dto.getCapital());
        c.setPlanPago(dto.getPlanPago());
        c.setCantCuotas(plan.getCantCuota());
        c.setPorcentajeInteres(plan.getTasaInteresTotal());
        c.setInteresesTotales(c.getCapital().multiply(c.getPorcentajeInteres()).divide(new BigDecimal(100)));
        c.setGastosAdministrativos(dto.getCapital().multiply(plan.getPorcentajeGastos()).divide(new BigDecimal(100)));
        c.setTotalCredito(c.getCapital().add(c.getInteresesTotales()).add(c.getGastosAdministrativos()));
        c.setObservaciones(dto.getObservaciones());
        creditoRepo.save(c);
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


}
