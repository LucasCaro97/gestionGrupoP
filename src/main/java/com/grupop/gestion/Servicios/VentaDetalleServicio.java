package com.grupop.gestion.Servicios;

import com.grupop.gestion.DTO.VentaDetalleDTO;
import com.grupop.gestion.Entidades.Producto;
import com.grupop.gestion.Entidades.Venta;
import com.grupop.gestion.Entidades.VentaDetalle;
import com.grupop.gestion.Repositorios.VentaDetalleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VentaDetalleServicio {

    private final VentaDetalleRepo ventaDetalleRepo;
    private final VentaServicio ventaServicio;
    private final ProductoServicio productoServicio;
    private final LoteServicio loteServicio;

    @Transactional
    public void crear(Long idVenta, Long idProd, Double cantidad, Double precioU) {

        Producto prod = productoServicio.buscarPorId(idProd);
        Venta venta = ventaServicio.obtenerPorId(idVenta);

        if (existByProductoAndVentaId(venta.getId(), prod.getId()) != 0) {
            VentaDetalle vtaDetalle = ventaDetalleRepo.searchByProductoAndVenta(venta.getId(), prod.getId());
            vtaDetalle.setCantidad(cantidad);
            vtaDetalle.setPrecioUnitario(precioU);
            vtaDetalle.setTotal(cantidad*precioU);
            ventaDetalleRepo.save(vtaDetalle);
            ventaServicio.actualizarTotalNuevo(idVenta);
        }
        else {
            VentaDetalle vtaDetalle = new VentaDetalle();
            vtaDetalle.setVentaId(venta);
            vtaDetalle.setProducto(prod);
            vtaDetalle.setCantidad(cantidad);
            vtaDetalle.setPrecioUnitario(precioU);
            vtaDetalle.setTotal(cantidad*precioU);
            ventaDetalleRepo.save(vtaDetalle);
            ventaServicio.actualizarTotalNuevo(idVenta);
        }
    }


    @Transactional
    public void crear(List<VentaDetalleDTO> listaItems) {
        for (VentaDetalleDTO v : listaItems ) {
            Producto prod = productoServicio.buscarPorId(v.getIdProd());
            Venta venta = ventaServicio.obtenerPorId(v.getIdVenta());

            if (existByProductoAndVentaId(venta.getId(), prod.getId()) != 0) {
                VentaDetalle vtaDetalle = ventaDetalleRepo.searchByProductoAndVenta(venta.getId(), prod.getId());
                vtaDetalle.setCantidad(v.getCantidad());
                vtaDetalle.setPrecioUnitario(v.getPrecioU());
                vtaDetalle.setTotal(v.getCantidad()*v.getPrecioU());
                ventaDetalleRepo.save(vtaDetalle);
            }
            else {
                VentaDetalle vtaDetalle = new VentaDetalle();
                vtaDetalle.setVentaId(venta);
                vtaDetalle.setProducto(prod);
                vtaDetalle.setCantidad(v.getCantidad());
                vtaDetalle.setPrecioUnitario(v.getPrecioU());
                vtaDetalle.setTotal(v.getCantidad()*v.getPrecioU());
                ventaDetalleRepo.save(vtaDetalle);

                productoServicio.actualizarEstadoPorId(v.getIdProd(), 0);
                loteServicio.alterarEstado(v.getIdProd(), 2l);
            }
        }
    }


    @Transactional
        public void eliminar (Long idVenta, Long idProd){
            VentaDetalle vtaDetalle = ventaDetalleRepo.searchByProductoAndVenta(idVenta,idProd);
            ventaDetalleRepo.deleteById(vtaDetalle.getId());

            productoServicio.actualizarEstadoPorId(idProd, 1);
            loteServicio.alterarEstado(idProd, 1l);
        }

        @Transactional(readOnly = true)
        public Integer existByProductoAndVentaId (Long ventaId, Long productoId){
            return ventaDetalleRepo.existByProductoAndVenta(ventaId, productoId);
        }

        @Transactional(readOnly = true)
        public List<VentaDetalle> obtenerPorVenta(Long id) {
           return ventaDetalleRepo.buscarPorVenta(id);
        }

        @Transactional(readOnly = true)
        public Double obtenerTotalPorVenta(Long id) { return ventaDetalleRepo.obtenerTotalPorVenta(id);}


}
