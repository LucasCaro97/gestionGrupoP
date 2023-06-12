package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VentaRepo extends JpaRepository<Venta, Long> {

    @Query( value= "SELECT MAX(id) FROM venta", nativeQuery = true)
    Long findLastId();


    @Query(value = "SELECT total FROM venta WHERE id = ?", nativeQuery = true)
    Double obtenerTotalPorId(Long id);

    @Query(value = "UPDATE venta SET total = (SELECT SUM(total) FROM venta_detalle WHERE fk_venta = 2) WHERE id = 2", nativeQuery = true)
    void recalcularTotal(@Param("ventaId") Long idVenta);

    // SELECT SUM(total) FROM venta_detalle WHERE fk_venta = 2
}
