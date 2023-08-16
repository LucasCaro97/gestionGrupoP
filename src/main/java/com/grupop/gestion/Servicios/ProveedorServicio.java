package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.EntidadBase;
import com.grupop.gestion.Entidades.Proveedor;
import com.grupop.gestion.Repositorios.ProveedorRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProveedorServicio {

    private final ProveedorRepo proveedorRepo;

    @Transactional
    public void crear() {
        Proveedor prov = new Proveedor();
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
    public List<Proveedor> obtenerTodos() { return proveedorRepo.findAll(); }
}
