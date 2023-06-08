package com.grupop.gestion.Servicios;

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

    public void crear(Long idVenta, Long idProd, Double cantidad, BigDecimal precioU) throws Exception {

        Producto prod = productoServicio.buscarPorId(idProd);
        Venta venta = ventaServicio.obtenerPorId(idVenta);

        if (existByProductoAndVentaId(venta.getId(), prod.getId()) != 0) {
            throw new Exception("Se omitio la creacion del producto ya existente en la venta");
        }

        VentaDetalle vtaDetalle = new VentaDetalle();
        vtaDetalle.setVentaId(venta);
        vtaDetalle.setProducto(prod);
        vtaDetalle.setCantidad(cantidad);
        vtaDetalle.setPrecioUnitario(precioU);
        BigDecimal cantidadBigDecimal = BigDecimal.valueOf(cantidad);
        vtaDetalle.setTotal(cantidadBigDecimal.multiply(precioU));
        ventaDetalleRepo.save(vtaDetalle);
        }



        public void eliminar (Long idVenta, String descProd){
            VentaDetalle vtaDetalle = ventaDetalleRepo.buscarPorVentayProducto(idVenta, descProd);
            ventaDetalleRepo.deleteById(vtaDetalle.getId());
        }

        public Integer existByProductoAndVentaId (Long ventaId, Long productoId){
            return ventaDetalleRepo.existByProductoAndVenta(ventaId, productoId);
        }

    @Transactional(readOnly = true)
    public List<VentaDetalle> obtenerPorVenta(Long id) {
       return ventaDetalleRepo.buscarPorVenta(id);
    }
}
