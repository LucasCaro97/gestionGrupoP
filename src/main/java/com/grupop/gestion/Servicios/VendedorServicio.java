package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.Vendedor;
import com.grupop.gestion.Repositorios.VendedorRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VendedorServicio {

    private final VendedorRepo vendedorRepo;

    @Transactional
    public void crear(){
        Vendedor vendedor = new Vendedor();
        vendedorRepo.save(vendedor);
    }

    @Transactional
    public void eliminarPorId(Long id){
        vendedorRepo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Vendedor buscarPorId(Long id){ return vendedorRepo.findById(id).get(); }

    @Transactional(readOnly = true)
    public Long buscarUltimoId(){ return vendedorRepo.findLastId(); }
}
