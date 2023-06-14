package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.CuentasContables;
import com.grupop.gestion.Entidades.Venta;
import com.grupop.gestion.Entidades.VentaDetalle;
import com.grupop.gestion.Entidades.VentaDetalleImputacion;
import com.grupop.gestion.Repositorios.VentaDetalleImputacionRepo;
import com.grupop.gestion.Repositorios.VentaDetalleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VentaDetalleImputacionServicio {

    private final VentaServicio ventaServicio;
    private final VentaDetalleImputacionRepo ventaDetalleImputacionRepo;
    private final CuentasContablesServicio cuentasContablesServicio;

    @Transactional
    public void crear(Long idVenta, Long idCuenta, Double importe){
        CuentasContables cuenta = cuentasContablesServicio.obtenerPorid(idCuenta);
        Venta venta = ventaServicio.obtenerPorId(idVenta);

        if(existByCuentaAndVentaId(venta.getId(), cuenta.getId()) != 0 ){
            VentaDetalleImputacion vdimp = ventaDetalleImputacionRepo.searchByCuentaAndVenta(venta.getId(), cuenta.getId());
            vdimp.setCuentasContables(cuentasContablesServicio.obtenerPorid(idCuenta));
            vdimp.setImporte(importe);
            vdimp.setVentaId(venta);
            ventaDetalleImputacionRepo.save(vdimp);
        }else{
            VentaDetalleImputacion vdimp = new VentaDetalleImputacion();
            vdimp.setCuentasContables(cuentasContablesServicio.obtenerPorid(idCuenta));
            vdimp.setImporte(importe);
            vdimp.setVentaId(venta);
            ventaDetalleImputacionRepo.save(vdimp);
        }
    }

    @Transactional
    public void eliminar(Long idVenta, Long idCuenta){
        VentaDetalleImputacion vdimp = ventaDetalleImputacionRepo.searchByCuentaAndVenta(idVenta,idCuenta);
        ventaDetalleImputacionRepo.deleteById(vdimp.getId());
    }

    private Integer existByCuentaAndVentaId(Long idVenta, Long idCuenta) {
        return ventaDetalleImputacionRepo.existByCuentaAndVenta(idVenta, idCuenta);
    }

    @Transactional
    public void actualizar(VentaDetalleImputacion dto){
        VentaDetalleImputacion vdimp = ventaDetalleImputacionRepo.findById(dto.getId()).get();
        vdimp.setCuentasContables(dto.getCuentasContables());
        vdimp.setImporte(dto.getImporte());
        ventaDetalleImputacionRepo.save(vdimp);
    }

    @Transactional(readOnly = true)
    public List<VentaDetalleImputacion> obtenerPorVenta(Long id){ return ventaDetalleImputacionRepo.buscarPorVenta(id); }
}
