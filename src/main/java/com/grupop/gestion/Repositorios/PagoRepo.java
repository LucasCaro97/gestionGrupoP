package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface PagoRepo extends JpaRepository<Pago, Long> {

    @Query(value = "SELECT MAX(id) FROM pago", nativeQuery = true)
    Long findLastId();

    @Query(value = "SELECT total FROM pago WHERE id = ?", nativeQuery = true)
    Double obtenerTotalPorId(Long id);


    @Query(value = "SELECT SUM(importe) FROM pago_detalle WHERE fk_pago = ?", nativeQuery = true)
    Optional<BigDecimal> obtenerTotalCtaCte(Long idPago);

    @Query(value = "SELECT SUM(importe) FROM pago_detalle_imputacion WHERE fk_pago = ?", nativeQuery = true)
    Optional<BigDecimal> obtenerTotalImp(Long idPago);


    @Query(value = "SELECT SUM(total) FROM pago WHERE DATE_FORMAT(fecha_comprobante, '%Y-%m') = DATE_FORMAT(CURRENT_DATE(), '%Y-%m')", nativeQuery = true)
    BigDecimal obtenerTotalPagadoMensual();

    Pago findTopByOrderByIdDesc();

    @Query(value = "SELECT SUM(importe) FROM pago_detalle_adelanto WHERE pago_id_id = ?", nativeQuery = true)
    Optional<BigDecimal> obtenerTotalAdelanto(Long idCobro);

    @Query(value = "SELECT fk_proveedor FROM pago WHERE id = ?", nativeQuery = true)
    Long obtenerProveedor(Long idOperacion);
}
