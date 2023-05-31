package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.TipoProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoProductoRepo extends JpaRepository<TipoProducto, Long> {

    @Query(value = "SELECT * FROM tipo_producto WHERE descripcion = ?", nativeQuery = true)
    TipoProducto obtenerTipoLote(String nombre);
}
