package com.grupop.gestion.Servicios;

import com.grupop.gestion.Entidades.Proveedor;
import com.grupop.gestion.Repositorios.ProveedorRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProveedorServicio {

        private final ProveedorRepo proveedorRepo;

        @Transactional
        public void crear(){
            Proveedor prov = new Proveedor();
            proveedorRepo.save(prov);
        }

        @Transactional
        public void eliminarPorId(Long id){ proveedorRepo.deleteById(id);  }
}
