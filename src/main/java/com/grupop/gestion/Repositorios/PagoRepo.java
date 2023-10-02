package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.Pago;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
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

    Page<Pago> findAllByOrderByIdDesc(Pageable pageable);

    @Query(value = "SELECT * FROM pago WHERE fecha_comprobante BETWEEN :fechaDesde AND :fechaHasta" +
            " AND (:sectorId IS NULL OR fk_sector = :sectorId) " +
            " AND (:talDesde IS NULL OR fk_talonario >= :talDesde) " +
            " AND (:talHasta IS NULL OR fk_talonario <= :talHasta) " +
            " AND (:idFormaPago IS NULL OR fk_forma_de_pago >= :idFormaPago) ",
            nativeQuery = true)
    List<Pago> obtenerOperacionesExcluyendoTalonario(String fechaDesde, String fechaHasta, Long sectorId, Integer talDesde, Integer talHasta, Long idFormaPago);

    @Query(value = "SELECT * FROM pago WHERE fecha_comprobante BETWEEN :fechaDesde AND :fechaHasta" +
            " AND (:sectorId IS NULL OR fk_sector = :sectorId) " +
            " AND (fk_talonario NOT BETWEEN :talDesde AND :talHasta) " +
            " AND (:idFormaPago IS NULL OR fk_forma_de_pago >= :idFormaPago) ",
            nativeQuery = true)
    List<Pago> obtenerOperaciones(String fechaDesde, String fechaHasta, Long sectorId, Integer talDesde, Integer talHasta, Long idFormaPago);

    @Query(value = "SELECT COUNT(*) FROM pago_detalle WHERE fk_pago = ?" , nativeQuery = true)
    Integer existsByPago(Long idPago);

    @Query(value = "SELECT COUNT(*) FROM pago_detalle_imputacion WHERE fk_pago = ?" , nativeQuery = true)
    Integer existsImputacionByPago(Long idPago);
}
