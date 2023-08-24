package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface CompraRepo extends JpaRepository<Compra,Long> {

    @Query(value = "SELECT MAX(id) FROM compra", nativeQuery = true)
    Long findLastId();

    @Query(value = "SELECT total FROM compra WHERE id = ?", nativeQuery = true)
    Double obtenerTotalPorId(Long id);

    @Query(value = "SELECT SUM(total) FROM compra_detalle WHERE fk_compra = ?", nativeQuery = true)
    Optional<BigDecimal> obtenerTotalProductos(Long idCompra);

    @Query(value = "SELECT SUM(importe_total) FROM compra_detalle_imputacion WHERE fk_compra = ?", nativeQuery = true)
    Optional<BigDecimal> obtenerTotalImputacion(Long idCompra);

    @Query(value = "SELECT * FROM compra WHERE fk_proveedor = ? AND bloqueado = 0", nativeQuery = true)
    List<Compra> obtenerComprasPendientesPagoPorProveedor(Long id);

    @Query(value = "SELECT bloqueado FROM compra WHERE id = ?", nativeQuery = true)
    Boolean validarEstado(Long idVenta);

    @Query(value = "SELECT SUM(total) FROM compra WHERE DATE_FORMAT(fecha_comprobante, '%Y-%m') = DATE_FORMAT(CURRENT_DATE(), '%Y-%m')", nativeQuery = true)
    BigDecimal obtenerTotalCompradoMensual();

    Compra findTopByOrderByIdDesc();

    @Query(value = "SELECT fk_proveedor FROM compra WHERE id = ? ", nativeQuery = true)
    Long obtenerCliente(Long idOperacion);
}