package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.PagoDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagoDetalleRepo extends JpaRepository<PagoDetalle, Long> {
    @Query(value = "SELECT * FROM pago_detalle WHERE fk_pago = ?", nativeQuery = true)
    List<PagoDetalle> obtenerPorPagoId(Long id);


    @Query(value = "SELECT COUNT(*) FROM pago_detalle WHERE fk_pago = ?2 AND fk_compra = ?1", nativeQuery = true)
    Integer existByCompraAndPago(Long idCompra, Long idPago);


    @Query(value = "SELECT * FROM pago_detalle WHERE fk_pago = ?2 AND fk_compra = ?1", nativeQuery = true)
    PagoDetalle searchByCompraAndPago(Long idCompra, Long idPago);
}
