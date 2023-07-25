package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.Compra;
import com.grupop.gestion.Entidades.Pago;
import com.grupop.gestion.Entidades.PagoDetalle;
import com.grupop.gestion.Repositorios.PagoDetalleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PagoDetalleServicioCtaCte {

    private final PagoServicio pagoServicio;
    private final PagoDetalleRepo pagoDetalleRepo;
    private final CompraServicio compraServicio;

    @Transactional
    public void crear(Long idPago, Long compraId, BigDecimal importe){
        Pago pago = pagoServicio.obtenerPorId(idPago);
        Compra compra = compraServicio.obtenerPorId(compraId);

        if(existByCompraAndPago(compra.getId(),pago.getId()) != 0){
            System.out.println("Ya existe, procesdo a actualizarlo");
            PagoDetalle p = pagoDetalleRepo.searchByCompraAndPago(compra.getId(),pago.getId());
            p.setPagoId(pago);
            p.setCompraId(compra);
            p.setImporte(importe);
            pagoDetalleRepo.save(p);
            compraServicio.marcarComoBloqueado(compra.getId());
        }else{
            System.out.println("Creando nuevo detalle");
            PagoDetalle p = new PagoDetalle();
            p.setPagoId(pago);
            p.setCompraId(compra);
            p.setImporte(importe);
            pagoDetalleRepo.save(p);
            compraServicio.marcarComoBloqueado(compra.getId());
        }

    }

    @Transactional
    public void eliminarPorId(Long id){
        PagoDetalle p = pagoDetalleRepo.findById(id).get();
        Compra compra = compraServicio.obtenerPorId(p.getCompraId().getId());
        compraServicio.marcarComoDesbloqueado(compra.getId());
        pagoDetalleRepo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<PagoDetalle> obtenerPorPagoId(Long id){
        return pagoDetalleRepo.obtenerPorPagoId(id);
    }

    public Integer existByCompraAndPago(Long idCompra, Long idPago){
        return pagoDetalleRepo.existByCompraAndPago(idCompra, idPago);

    }


}

