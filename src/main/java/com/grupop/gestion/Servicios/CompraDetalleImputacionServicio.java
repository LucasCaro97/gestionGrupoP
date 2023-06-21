package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.Compra;
import com.grupop.gestion.Entidades.CompraDetalleImputacion;
import com.grupop.gestion.Entidades.CuentasContables;
import com.grupop.gestion.Repositorios.CompraDetalleImputacionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompraDetalleImputacionServicio {

    private final CompraServicio compraServicio;
    private final CompraDetalleImputacionRepo compraDetalleImputacionRepo;
    private final CuentasContablesServicio cuentasContablesServicio;

    @Transactional
    public void crear(Long idCompra, Long idCuenta, Double importe) {
        CuentasContables cuenta = cuentasContablesServicio.obtenerPorid(idCuenta);
        Compra compra = compraServicio.obtenerPorId(idCompra);

        if (existByCompraAndCuenta(compra.getId(), cuenta.getId()) != 0) {
            CompraDetalleImputacion cdimp = compraDetalleImputacionRepo.seachByCuentaAndCompra(compra.getId(), cuenta.getId());
            cdimp.setCuentasContables(cuentasContablesServicio.obtenerPorid(idCuenta));
            cdimp.setImporte(importe);
            cdimp.setCompraId(compra);
            compraDetalleImputacionRepo.save(cdimp);
        } else {
            CompraDetalleImputacion cdimp = new CompraDetalleImputacion();
            cdimp.setCuentasContables(cuentasContablesServicio.obtenerPorid(idCuenta));
            cdimp.setImporte(importe);
            cdimp.setCompraId(compra);
            compraDetalleImputacionRepo.save(cdimp);
        }

    }

    @Transactional
    public void eliminar(Long idCompra, Long idCuenta){
        CompraDetalleImputacion cdimp = compraDetalleImputacionRepo.seachByCuentaAndCompra(idCompra, idCuenta);
        compraDetalleImputacionRepo.deleteById(cdimp.getId());
    }

    @Transactional(readOnly = true)
    private Integer existByCompraAndCuenta(Long idCompra, Long idCuenta) {
        return compraDetalleImputacionRepo.existsByCuentaAndCompra(idCompra, idCuenta);
    }

    @Transactional
    public void actualizar(CompraDetalleImputacion dto){
        CompraDetalleImputacion cdimp = compraDetalleImputacionRepo.findById(dto.getId()).get();
        cdimp.setCuentasContables(dto.getCuentasContables());
        cdimp.setImporte(dto.getImporte());
        compraDetalleImputacionRepo.save(cdimp);
    }

    @Transactional(readOnly = true)
    public List<CompraDetalleImputacion> obtenerPorCompra(Long id){ return compraDetalleImputacionRepo.buscarPorCompra(id); }

}
