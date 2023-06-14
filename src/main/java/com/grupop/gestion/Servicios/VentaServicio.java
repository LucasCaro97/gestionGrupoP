package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.Venta;
import com.grupop.gestion.Repositorios.TalonarioRepo;
import com.grupop.gestion.Repositorios.VentaRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VentaServicio {


    private final VentaRepo ventaRepo;
    private final TalonarioServicio talonarioServicio;

    @Transactional
    public void crear(Venta dto){
        Venta vta = new Venta();
        vta.setCliente(dto.getCliente());
        vta.setCuit(dto.getCuit());
        vta.setTipoIva(dto.getTipoIva());
        vta.setTipoComprobante(dto.getTipoComprobante());
        vta.setTalonario(dto.getTalonario());
        vta.setNroComprobante(dto.getNroComprobante());
        vta.setSector(dto.getSector());
        vta.setMoneda(dto.getMoneda());
        vta.setFormaDePago(dto.getFormaDePago());
        vta.setObservaciones(dto.getObservaciones());
        vta.setFechaComprobante(dto.getFechaComprobante());
        vta.setTotal(0D);
        talonarioServicio.aumentarUltimoNro(dto.getTalonario());
        ventaRepo.save(vta);
    }

    @Transactional //AGREGAR VALIDACIONES PARA QUE NO SE PUEDA MODIFICAR CUANDO YA CONTIENE UN COBRO
    public void actualizar(Venta dto){
        Venta vta = ventaRepo.findById(dto.getId()).get();
        vta.setCliente(dto.getCliente());
        vta.setCuit(dto.getCuit());
        vta.setTipoIva(dto.getTipoIva());
        vta.setTipoComprobante(dto.getTipoComprobante());
        vta.setTalonario(dto.getTalonario());
        vta.setNroComprobante(dto.getNroComprobante());
        vta.setSector(dto.getSector());
        vta.setMoneda(dto.getMoneda());
        vta.setFormaDePago(dto.getFormaDePago());
        vta.setObservaciones(dto.getObservaciones());
        vta.setFechaComprobante(dto.getFechaComprobante());
        ventaRepo.save(vta);
    }


    @Transactional(readOnly = true)
    public List<Venta> obtenerTodas(){ return ventaRepo.findAll(); }

    @Transactional(readOnly = true)
    public Venta obtenerPorId(Long id){ return ventaRepo.findById(id).get(); }

    @Transactional
    public void eliminarPorId(Long id){ ventaRepo.deleteById(id); }

    @Transactional(readOnly = true)
    public Long buscarUltimoId(){ return ventaRepo.findLastId(); }

    @Transactional
    public void actualizarTotal(Long idVenta, Double total) {
        Venta vta = ventaRepo.findById(idVenta).get();
        vta.setTotal(total);
        ventaRepo.save(vta);
    }
    @Transactional(readOnly = true)
    public Double obtenerTotalPorId(Long id){
        return ventaRepo.obtenerTotalPorId(id);
    }

}

