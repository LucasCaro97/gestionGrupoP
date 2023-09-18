package com.grupop.gestion.Servicios;

import com.grupop.gestion.DTO.CobroDetalleCtaCteDTO;
import com.grupop.gestion.Entidades.CobroDetalleCtaCte;
import com.grupop.gestion.Entidades.CobroDetalleCuotas;
import com.grupop.gestion.Entidades.Producto;
import com.grupop.gestion.Entidades.VentaDetalle;
import com.grupop.gestion.Repositorios.CobroDetalleCtaCteRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CobroDetalleCtaCteServicio {

    private final CobroDetalleCtaCteRepo cobroDetalleCtaCteRepo;
    private final CobroServicio cobroServicio;
    private final VentaServicio ventaServicio;
    private final VentaDetalleServicio ventaDetalleServicio;
    private final ProductoServicio productoServicio;
    private final LoteServicio loteServicio;

    @Transactional
    public void crear (Long idCobro, Long idVenta, BigDecimal importe){
        if(cobroDetalleCtaCteRepo.existByVentaId(idVenta) != 0){
            CobroDetalleCtaCte c = cobroDetalleCtaCteRepo.searchByVentaId(idVenta);
            c.setCobroId(cobroServicio.obtenerPorId(idCobro));
            c.setVentaId(ventaServicio.obtenerPorId(idVenta));
            c.setTotalDetalle(importe);
            cobroDetalleCtaCteRepo.save(c);
        }else{
            CobroDetalleCtaCte c = new CobroDetalleCtaCte();
            c.setCobroId(cobroServicio.obtenerPorId(idCobro));
            c.setVentaId(ventaServicio.obtenerPorId(idVenta));
            c.setTotalDetalle(importe);
            cobroDetalleCtaCteRepo.save(c);

            //CIERRO LA VENTA PARA QUE NO SE PUEDA MODIFICAR NADA
            ventaServicio.cerrarVenta(idVenta, true);


        }


    }

    @Transactional
    public void crear (List<CobroDetalleCtaCteDTO> listaItems){
        for (CobroDetalleCtaCteDTO cobroItem : listaItems ) {
            if(cobroDetalleCtaCteRepo.existByVentaId(cobroItem.getIdVenta()) != 0){
                CobroDetalleCtaCte c = cobroDetalleCtaCteRepo.searchByVentaId(cobroItem.getIdVenta());
                c.setCobroId(cobroServicio.obtenerPorId(cobroItem.getIdCobro()));
                c.setVentaId(ventaServicio.obtenerPorId(cobroItem.getIdVenta()));
                c.setTotalDetalle(cobroItem.getImporte());
                cobroDetalleCtaCteRepo.save(c);
            }else{
                CobroDetalleCtaCte c = new CobroDetalleCtaCte();
                c.setCobroId(cobroServicio.obtenerPorId(cobroItem.getIdCobro()));
                c.setVentaId(ventaServicio.obtenerPorId(cobroItem.getIdVenta()));
                c.setTotalDetalle(cobroItem.getImporte());
                cobroDetalleCtaCteRepo.save(c);
//                CIERRO LA VENTA PARA QUE NO SE PUEDA MODIFICAR NADA
                ventaServicio.cerrarVenta(cobroItem.getIdVenta(), true);

                //SETEO EL/LOS LOTE/LOTES COMO VENDIDO YA QUE SE HA COBRADO LA TOTALIDAD DE LA VENTA
                List<VentaDetalle> listaItemsVta = ventaDetalleServicio.obtenerPorVenta(cobroItem.getIdVenta());
                for (VentaDetalle v : listaItemsVta ) {
                    loteServicio.alterarEstado(v.getProducto().getId(), 3l);
                }


            }
        }


    }

    @Transactional(readOnly = true)
    public List<CobroDetalleCtaCte> obtenerTodos(){
        return cobroDetalleCtaCteRepo.findAll();
    }

    @Transactional
    public void eliminar(Long idDetalle){
        CobroDetalleCtaCte c = cobroDetalleCtaCteRepo.findById(idDetalle).get();
        cobroDetalleCtaCteRepo.deleteById(idDetalle);
        ventaServicio.cerrarVenta(c.getVentaId().getId(), false);

        List<VentaDetalle> listaItemsVta = ventaDetalleServicio.obtenerPorVenta(c.getVentaId().getId());
        for (VentaDetalle v : listaItemsVta ) {
            loteServicio.alterarEstado(v.getProducto().getId(), 2l);
        }
    }

    @Transactional(readOnly = true)
    public List<CobroDetalleCtaCte> obtenerTodosPorCobro(Long id) {
        return cobroDetalleCtaCteRepo.obtenerPorCobro(id);
    }
}
