package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.Comision;
import com.grupop.gestion.Repositorios.ComisionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ComisionServicio {

    private final ComisionRepo comisionRepo;
    private final VentaServicio ventaServicio;
    private final VendedorServicio vendedorServicio;

    @Transactional
    public void crear(Long idVendedor, Long idVenta,BigDecimal baseImp, BigDecimal porcentajeComision){


        Comision c = new Comision();
        c.setVenta(ventaServicio.obtenerPorId(idVenta));
        c.setVendedor(vendedorServicio.buscarPorId(idVendedor));
        c.setBaseImponible(baseImp);
        c.setPorcentajeComisionGeneral(porcentajeComision);
        c.setComisionGeneral(c.getBaseImponible().multiply(c.getPorcentajeComisionGeneral()).divide(new BigDecimal(100)));
        comisionRepo.save(c);
    }

    @Transactional(readOnly = true)
    public List<Comision> obtenerComisionVenta(Long idVenta){
       return comisionRepo.obtenerComisionVenta(idVenta);
    }

    @Transactional
    public void eliminarPorId(Long idComision) {
        comisionRepo.deleteById(idComision);
    }

    @Transactional(readOnly = true)
    public Comision obtenerPorId(Long id){
        return comisionRepo.findById(id).get();
    }

    @Transactional(readOnly = true)
    public List<Comision> obtenerTodas() {
        return comisionRepo.findAll();
    }
}
