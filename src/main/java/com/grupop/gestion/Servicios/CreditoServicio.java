package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.Credito;
import com.grupop.gestion.Repositorios.CreditoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreditoServicio {

    private final CreditoRepo creditoRepo;
    private final VentaServicio ventaServicio;


    @Transactional
    public void crear(Credito dto){
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
        c.setInteresesTotales(dto.getInteresesTotales());
        c.setGastosAdministrativos(dto.getGastosAdministrativos());
        c.setCapital(dto.getCapital());
        c.setTotalCredito(dto.getTotalCredito());
        c.setObservaciones(dto.getObservaciones());
        System.out.println(dto);
        creditoRepo.save(c);

        //CIERRO LA VENTA PARA QUE NO SE PUEDA MODIFICAR NADA
        System.out.println("cerrando venta: " + dto.getVenta().getId());
        ventaServicio.cerrarVenta(dto.getVenta().getId());

    }

    @Transactional
    public void actualizar(Credito dto){
        Credito c = creditoRepo.findById(dto.getId()).get();
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
        c.setInteresesTotales(dto.getInteresesTotales());
        c.setGastosAdministrativos(dto.getGastosAdministrativos());
        c.setCapital(dto.getCapital());
        c.setTotalCredito(dto.getTotalCredito());
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
