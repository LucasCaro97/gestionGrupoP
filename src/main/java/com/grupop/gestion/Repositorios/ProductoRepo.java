package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.Lote;
import com.grupop.gestion.Entidades.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepo extends JpaRepository<Producto, Long> {

    boolean existsByLote(Lote lote);

    @Query(value = "UPDATE lote SET es_producto = 0 WHERE id > 0", nativeQuery = true)
    boolean bajaProductoLote();

}
