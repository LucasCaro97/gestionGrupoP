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
    public void crear(Long idCompra, Long idProd, BigDecimal cantidad, BigDecimal precioU, BigDecimal precioF){

        Producto prod = productoServicio.buscarPorId(idProd);
        Compra compra = compraServicio.obtenerPorId(idCompra);

        if(existByProductoAndCompraId(compra.getId(), prod.getId()) != 0){
            CompraDetalle cd = compraDetalleRepo.searchByProductoAndCompra(compra.getId(), prod.getId());
            cd.setCantidad(cantidad);
                if( precioF.compareTo(cd.getPrecioFinal()) != 0){
                    cd.setPrecioUnitario(precioF.divide(new BigDecimal(1.21),2,RoundingMode.UP));
                    cd.setPrecioFinal(precioF);
                }else{
                    cd.setPrecioUnitario(precioU);
                    cd.setPrecioFinal(precioU.multiply(new BigDecimal(1.21).setScale(2,RoundingMode.UP)));
                }
            cd.setTotalsinImpuesto(cd.getPrecioUnitario().multiply(cantidad));
            cd.setImpuesto(cd.getTotalsinImpuesto().multiply(new BigDecimal(21)).divide(new BigDecimal(100),2,RoundingMode.UP));
            cd.setTotal(cd.getTotalsinImpuesto().add(cd.getImpuesto()));
            compraDetalleRepo.save(cd);
        }else{
            CompraDetalle cd = new CompraDetalle();
            cd.setCompraId(compra);
            cd.setProducto(prod);
            cd.setCantidad(cantidad);
            if(precioF.compareTo(BigDecimal.ZERO) != 0){
                cd.setPrecioUnitario(precioF.divide(new BigDecimal(1.21d),2,RoundingMode.UP));
                cd.setPrecioFinal(precioF);
                cd.setTotalsinImpuesto(cantidad.multiply(cd.getPrecioUnitario()).setScale(2,RoundingMode.UP));
                cd.setImpuesto((cd.getTotalsinImpuesto().multiply(new BigDecimal(21))).divide(new BigDecimal(100),2,RoundingMode.UP));
                cd.setTotal(cd.getTotalsinImpuesto().add(cd.getImpuesto()));

            }else{
                cd.setPrecioUnitario(precioU);
                cd.setPrecioFinal(precioU.multiply(new BigDecimal(1.21d)).setScale(2,RoundingMode.UP));
                cd.setTotalsinImpuesto(precioU.multiply(cantidad).setScale(2,RoundingMode.UP));
                cd.setImpuesto(cd.getTotalsinImpuesto().multiply(new BigDecimal(21)).divide(new BigDecimal(100),2,RoundingMode.UP));
                cd.setTotal(cd.getTotalsinImpuesto().add(cd.getImpuesto()));
            }

            compraDetalleRepo.save(cd);
        }

    }

    @Transactional
    public void eliminar(Long idCompra, Long idProd){
        CompraDetalle cd = compraDetalleRepo.searchByProductoAndCompra(idCompra, idProd);
        compraDetalleRepo.deleteById(cd.getId());
    }

    @Transactional(readOnly = true)
    public Integer existByProductoAndCompraId(Long compraId, Long productoId){
        return compraDetalleRepo.existByProductoAndCompra(compraId,productoId);
    }

    @Transactional(readOnly = true)
    public List<CompraDetalle> obtenerPorCompra(Long id){ return compraDetalleRepo.buscarPorCompra(id); }

    @Transactional Double obtenerTotalPorCompra(Long id) { return compraDetalleRepo.obtenerTotalPorCompra(id); }





}
