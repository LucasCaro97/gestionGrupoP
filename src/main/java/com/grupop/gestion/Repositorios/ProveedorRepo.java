package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.EntidadBase;
import com.grupop.gestion.Entidades.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProveedorRepo extends JpaRepository<Proveedor,Long> {


    @Query( value= "SELECT MAX(id_proveedor) FROM proveedor", nativeQuery = true)
    Long findLastId();

    /*
    @Query( value= "SELECT id_proveedor, razon_social FROM proveedor INNER JOIN entidad_base ON proveedor.id_proveedor = entidad_base.fk_proveedor", nativeQuery = true)
    List<Proveedor> findSupplier();
    */
}
