package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.EntidadBase;
import com.grupop.gestion.Entidades.Proveedor;
import com.grupop.gestion.Repositorios.ProveedorRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProveedorServicio {

    private final ProveedorRepo proveedorRepo;

    @Transactional
    public void crear() {
        Proveedor prov = new Proveedor();
        prov.setSaldoAFavor(BigDecimal.ZERO);
        proveedorRepo.save(prov);

    }

    @Transactional
    public void eliminarPorId(Long id) {
        proveedorRepo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Long buscarUltimoId() {
        return proveedorRepo.findLastId();
    }

    @Transactional(readOnly = true)
    public Proveedor buscarPorId(Long id) {
        return proveedorRepo.findById(id).get();
    }

    @Transactional(readOnly = true)
    public String obtenerNombre(Long id) { return proveedorRepo.obtenerNombre(id);}

    @Transactional(readOnly = true)
    public List<Proveedor> obtenerTodos() { return proveedorRepo.findAll(); }

    @Transactional
    public void actualizarSaldoAFavor(Long idProv, BigDecimal importe){
        Proveedor p = proveedorRepo.findById(idProv).get();
        p.setSaldoAFavor(p.getSaldoAFavor().add(importe));
        proveedorRepo.save(p);
    }

    @Transactional
    public void descontarSaldoAFavor(Long idProv, BigDecimal importe){
        Proveedor p = proveedorRepo.findById(idProv).get();
        p.setSaldoAFavor(p.getSaldoAFavor().subtract(importe));
        proveedorRepo.save(p);
    }

    @Transactional
    public void devolverSaldoAFavor(Long idProv, BigDecimal monto) {
        Proveedor p = proveedorRepo.findById(idProv).get();
        p.setSaldoAFavor(p.getSaldoAFavor().add(monto));
        proveedorRepo.save(p);
    }
    @Transactional(readOnly = true)
    public BigDecimal obtenerSaldo(Long idProveedor){
        return proveedorRepo.obtenerSaldoPorIdProveedor(idProveedor);
    }

}
