package com.grupop.gestion.Servicios;

import com.grupop.gestion.DTO.ImpuestoDto;
import com.grupop.gestion.Entidades.Compra;
import com.grupop.gestion.Entidades.CompraDetalleImputacion;
import com.grupop.gestion.Entidades.CuentasContables;
import com.grupop.gestion.Entidades.Impuestos;
import com.grupop.gestion.Repositorios.CompraDetalleImputacionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompraDetalleImputacionServicio {

    private final CompraServicio compraServicio;
    private final CompraDetalleImputacionRepo compraDetalleImputacionRepo;
    private final CuentasContablesServicio cuentasContablesServicio;
    private final ImpuestosServicio impuestosServicio;

    @Transactional
    public void crear(Long idCompra, Long idCuenta, BigDecimal importe, List<Long> impuestosIds) {
        CuentasContables cuenta = cuentasContablesServicio.obtenerPorid(idCuenta);
        Compra compra = compraServicio.obtenerPorId(idCompra);


        if (existByCompraAndCuenta(compra.getId(), cuenta.getId()) != 0) {
            CompraDetalleImputacion cdimp = compraDetalleImputacionRepo.seachByCuentaAndCompra(compra.getId(), cuenta.getId());
            List<Impuestos> listaImpuestosItem = cdimp.getImpuestos();
            BigDecimal totalImpuesto = new BigDecimal(0);
            BigDecimal totalLinea = new BigDecimal(0);
            cdimp.setImporteBase(importe);
            for(Impuestos i : listaImpuestosItem){
                totalImpuesto = cdimp.getImporteBase().multiply(new BigDecimal(i.getPorcentaje())).divide(new BigDecimal(100));
                totalLinea = totalLinea.add(totalImpuesto);
            }
            cdimp.setImporteTotal(cdimp.getImporteBase().add(totalLinea));
            compraDetalleImputacionRepo.save(cdimp);
            compraServicio.actualizarTotal(idCompra);
        } else {
            BigDecimal totalImpuesto = new BigDecimal(0);
            List<Impuestos> impuestos = impuestosServicio.obtenerImpuestosPorIds(impuestosIds);
            BigDecimal totalLinea = new BigDecimal(0);
            CompraDetalleImputacion cdimp = new CompraDetalleImputacion();
            cdimp.setCuentasContables(cuentasContablesServicio.obtenerPorid(idCuenta));
            cdimp.setImporteBase(importe);
            cdimp.setCompraId(compra);
            cdimp.setImpuestos(impuestos);
            for(Impuestos i : impuestos){
                totalImpuesto = cdimp.getImporteBase().multiply(new BigDecimal(i.getPorcentaje())).divide(new BigDecimal(100)) ;
                totalLinea = totalLinea.add(totalImpuesto);
            }
            cdimp.setImporteTotal(cdimp.getImporteBase().add(totalLinea));
            compraDetalleImputacionRepo.save(cdimp);
            compraServicio.actualizarTotal(idCompra);
        }

    }

    @Transactional
    public void eliminar(Long idCompra, Long idCuenta){
        CompraDetalleImputacion cdimp = compraDetalleImputacionRepo.seachByCuentaAndCompra(idCompra, idCuenta);
        compraDetalleImputacionRepo.deleteById(cdimp.getId());
        compraServicio.actualizarTotal(idCompra);
    }

    @Transactional(readOnly = true)
    private Integer existByCompraAndCuenta(Long idCompra, Long idCuenta) {
        return compraDetalleImputacionRepo.existsByCuentaAndCompra(idCompra, idCuenta);
    }

    @Transactional
    public void actualizar(CompraDetalleImputacion dto){
        CompraDetalleImputacion cdimp = compraDetalleImputacionRepo.findById(dto.getId()).get();
        cdimp.setCuentasContables(dto.getCuentasContables());
        cdimp.setImporteBase(dto.getImporteBase());
        compraDetalleImputacionRepo.save(cdimp);
    }

    @Transactional(readOnly = true)
    public List<CompraDetalleImputacion> obtenerPorCompra(Long id){ return compraDetalleImputacionRepo.buscarPorCompra(id); }

}
