package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.CobroDetalleCtaCte;
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

    @Transactional
    public void crear (Long idCobro, Long idVenta, BigDecimal importe){
        if(cobroDetalleCtaCteRepo.existByVentaId(idVenta) != 0){
            System.out.println("Ya existe el cobro, procedo a actualizarlo cta cte");
            CobroDetalleCtaCte c = cobroDetalleCtaCteRepo.searchByVentaId(idVenta);
            c.setCobroId(cobroServicio.obtenerPorId(idCobro));
            c.setVentaId(ventaServicio.obtenerPorId(idVenta));
            c.setTotalDetalle(importe);
            cobroDetalleCtaCteRepo.save(c);
        }else{
            System.out.println("Creando nuevo item de cobro cta cte");
            CobroDetalleCtaCte c = new CobroDetalleCtaCte();
            c.setCobroId(cobroServicio.obtenerPorId(idCobro));
            c.setVentaId(ventaServicio.obtenerPorId(idVenta));
            c.setTotalDetalle(importe);
            cobroDetalleCtaCteRepo.save(c);

            //CIERRO LA VENTA PARA QUE NO SE PUEDA MODIFICAR NADA
            System.out.println("cerrando venta: " + idVenta);
            ventaServicio.cerrarVenta(idVenta);

        }


        }

    @Transactional(readOnly = true)
    public List<CobroDetalleCtaCte> obtenerTodos(){
        return cobroDetalleCtaCteRepo.findAll();
    }

    @Transactional
    public void eliminar(Long idDetalle){ cobroDetalleCtaCteRepo.deleteById(idDetalle);}

    @Transactional(readOnly = true)
    public List<CobroDetalleCtaCte> obtenerTodosPorCobro(Long id) {
        return cobroDetalleCtaCteRepo.obtenerPorCobro(id);
    }
}
