package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.CompraDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompraDetalleRepo extends JpaRepository<CompraDetalle, Long> {

    @Query(value = "SELECT COUNT(*) FROM compra_detalle WHERE fk_compra = ?1 AND fk_producto = ?2", nativeQuery = true)
    Integer existByProductoAndCompra(Long idCompra, Long idProducto);

    @Query(value = "SELECT * FROM compra_detalle WHERE fk_compra = ?1 AND fk_producto = ?2", nativeQuery = true)
    CompraDetalle searchByProductoAndCompra(Long idCompra, Long idProducto);

    @Query(value = "SELECT * FROM compra_detalle WHERE fk_compra = ?1", nativeQuery = true)
    List<CompraDetalle> buscarPorCompra(Long id);

    @Query(value = "SELECT SUM(total) FROM compra_detalle WHERE fk_compra ?", nativeQuery = true)
    Double obtenerTotalPorCompra(Long id);

}
