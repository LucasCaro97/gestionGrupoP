package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProveedorRepo extends JpaRepository<Proveedor,Long> {


    @Query( value= "SELECT MAX(id) FROM proveedor", nativeQuery = true)
    Long findLastId();
}
