package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.Cliente;
import com.grupop.gestion.Entidades.EntidadBase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntidadBaseRepo extends JpaRepository<EntidadBase,Long> {


        @Query(value = "SELECT * FROM entidad_base WHERE fk_cliente > 0", nativeQuery = true)
        List<EntidadBase> findClients();

        @Query(value = "SELECT * FROM entidad_base WHERE fk_proveedor > 0", nativeQuery = true)
        List<EntidadBase> findSuppliers();
        @Query(value = "SELECT * FROM entidad_base WHERE fk_empleado > 0", nativeQuery = true)
        List<EntidadBase> findEmployees();

        @Query(value = "SELECT * FROM entidad_base WHERE fk_vendedor > 0", nativeQuery = true)
        List<EntidadBase> findSellers();

}
