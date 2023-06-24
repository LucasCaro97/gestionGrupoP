package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.Compra;
import com.grupop.gestion.Entidades.CompraDetalle;
import com.grupop.gestion.Entidades.Producto;
import com.grupop.gestion.Repositorios.CompraDetalleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompraDetalleServicio {

    private final CompraDetalleRepo compraDetalleRepo;
    private final CompraServicio compraServicio;
    private final ProductoServicio productoServicio;


    @Transactional
    public void crear(Long idCompra, Long idProd, BigDecimal cantidad, BigDecimal precioU, BigDecimal precioF) {

        Producto prod = productoServicio.buscarPorId(idProd);
        Compra compra = compraServicio.obtenerPorId(idCompra);
        CompraDetalle cd = compraDetalleRepo.searchByProductoAndCompra(compra.getId(), prod.getId());
        System.out.println("Detalle: " + cd);

        //VERIFICO QUE HAYA ALGUN CAMBIO RELEVANTE EN LA TABLA PARA RECALCULAR ( CAMBIO DE CANT / $INI / $FIN )
        if (existByProductoAndCompraId(compra.getId(), prod.getId()) != 0) {
            System.out.println("Detalle en if exist: " + cd);
            if (cd.getCantidad().compareTo(cantidad) != 0 || cd.getPrecioUnitario().compareTo(precioU) != 0 || cd.getPrecioFinal().compareTo(precioF) != 0) {
                cd.setCantidad(cantidad);
                if (precioF.compareTo(cd.getPrecioFinal()) != 0) {
                    BigDecimal precioUnitario = precioF.divide(new BigDecimal(1.21), 4, RoundingMode.UP);
                    cd.setPrecioUnitario(precioUnitario);
                    cd.setPrecioFinal(precioF);
                } else {
                    cd.setPrecioUnitario(precioU);
                    BigDecimal precioFinal = precioU.multiply(new BigDecimal(1.21)).setScale(4, RoundingMode.UP);
                    cd.setPrecioFinal(precioFinal);
                }

                BigDecimal totalSinImpuesto = cd.getCantidad().multiply(cd.getPrecioUnitario());
                cd.setTotalsinImpuesto(totalSinImpuesto);
                cd.setImpuesto(cd.getTotalsinImpuesto().multiply(new BigDecimal(21)).divide(new BigDecimal(100)));
                cd.setTotal(cd.getPrecioFinal().multiply(cd.getCantidad()));

                compraDetalleRepo.save(cd);
            }else{
                System.out.println("No se registraron cambios");
            }
        }else {
                System.out.println("Creando item");
                CompraDetalle compradet = new CompraDetalle();
                compradet.setCompraId(compra);
                compradet.setProducto(prod);
                compradet.setCantidad(cantidad);
                if (precioF.compareTo(BigDecimal.ZERO) != 0) {
                    BigDecimal precioUnitario = precioF.divide(new BigDecimal(1.21), 4, RoundingMode.UP);
                    BigDecimal totalSinImpuesto = cantidad.multiply(precioUnitario).setScale(4, RoundingMode.UP);
                    BigDecimal impuesto = totalSinImpuesto.multiply(new BigDecimal(21)).divide(new BigDecimal(100), 4, RoundingMode.UP);
                    BigDecimal total = precioF.multiply(compradet.getCantidad()).setScale(4);

                    compradet.setPrecioUnitario(precioUnitario.setScale(2, RoundingMode.UP));
                    compradet.setPrecioFinal(precioF);
                    compradet.setTotalsinImpuesto(totalSinImpuesto.setScale(2, RoundingMode.UP));
                    compradet.setImpuesto(impuesto.setScale(2, RoundingMode.UP));
                    compradet.setTotal(total.setScale(2, RoundingMode.UP));

                } else {
                    BigDecimal precioFinal = precioU.multiply(new BigDecimal(1.21).setScale(4, RoundingMode.UP));
                    BigDecimal totalSinImpuesto = cantidad.multiply(precioU).setScale(4, RoundingMode.UP);
                    BigDecimal impuesto = totalSinImpuesto.multiply(new BigDecimal(21)).divide(new BigDecimal(100), 4, RoundingMode.UP);
                    BigDecimal total = precioFinal.multiply(compradet.getCantidad()).setScale(4, RoundingMode.UP);

                    compradet.setPrecioUnitario(precioU);
                    compradet.setPrecioFinal(precioFinal);
                    compradet.setTotalsinImpuesto(totalSinImpuesto.setScale(2, RoundingMode.UP));
                    compradet.setImpuesto(impuesto.setScale(2, RoundingMode.UP));
                    compradet.setTotal(total.setScale(2, RoundingMode.UP));
                }

                compraDetalleRepo.save(compradet);
            }
        }

    @Transactional
    public void eliminar(Long idCompra, Long idProd) {
        CompraDetalle cd = compraDetalleRepo.searchByProductoAndCompra(idCompra, idProd);
        compraDetalleRepo.deleteById(cd.getId());
    }

    @Transactional(readOnly = true)
    public Integer existByProductoAndCompraId(Long compraId, Long productoId) {
        return compraDetalleRepo.existByProductoAndCompra(compraId, productoId);
    }

    @Transactional(readOnly = true)
    public List<CompraDetalle> obtenerPorCompra(Long id) {
        return compraDetalleRepo.buscarPorCompra(id);
    }

    @Transactional
    Double obtenerTotalPorCompra(Long id) {
        return compraDetalleRepo.obtenerTotalPorCompra(id);
    }


}
