package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.Cliente;
import com.grupop.gestion.Entidades.IndiceCAC;
import com.grupop.gestion.Entidades.Venta;
import com.grupop.gestion.Entidades.VentaDetalle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface VentaRepo extends JpaRepository<Venta, Long> {


    @Query( value= "SELECT MAX(id) FROM venta", nativeQuery = true)
    Long findLastId();

    @Query(value = "SELECT total FROM venta WHERE id = ?", nativeQuery = true)
    Double obtenerTotalPorId(Long id);

    @Query(value = "SELECT bloqueada FROM venta WHERE id = ?", nativeQuery = true)
    Boolean validarEstado(Long idVenta);

    @Query(value = "SELECT * FROM venta WHERE bloqueada = 0 AND fk_cliente = ?", nativeQuery = true)
    List<Venta> obtenerVentasSinCreditoPorCliente(Long id);


    @Query(value = "SELECT SUM(total) FROM venta_detalle WHERE fk_venta = ?",nativeQuery = true)
    Optional<BigDecimal> obtenerTotalProductos(Long id);

    @Query(value = "SELECT SUM(importe) FROM venta_detalle_imputacion WHERE fk_venta = ?", nativeQuery = true)
    Optional<BigDecimal> obtenerTotalImputacion(Long id);

    @Query(value = "SELECT * FROM venta WHERE fk_cliente = ? AND bloqueada = 0", nativeQuery = true)
    List<Venta> obtenerVentasCtaCtePorCliente(Long id);

    @Query(value = "SELECT fk_indice_base FROM venta WHERE id = ?1", nativeQuery = true)
    Long obtenerIndiceBase(Long idVenta);

    @Query(value = "SELECT SUM(total) FROM venta WHERE fk_moneda = 1 AND DATE_FORMAT(fecha_comprobante, '%Y-%m') = DATE_FORMAT(CURRENT_DATE(), '%Y-%m')", nativeQuery = true)
    BigDecimal obtenerTotalVendidoMensual();

    Venta findTopByOrderByIdDesc();

    @Query(value = "SELECT fk_cliente FROM venta WHERE id = ? ", nativeQuery = true)
    Long obtenerCliente(Long idOperacion);

    Page<Venta> findAllByOrderByIdDesc(Pageable pageable);

    @Query(value = "SELECT * FROM venta WHERE fecha_comprobante BETWEEN :fechaDesde AND :fechaHasta" +
            " AND (:sectorId IS NULL OR fk_sector = :sectorId) " +
            " AND (:talDesde IS NULL OR fk_talonario >= :talDesde) " +
            " AND (:talHasta IS NULL OR fk_talonario <= :talHasta) " +
            " AND (:idFormaPago IS NULL OR fk_forma_de_pago >= :idFormaPago) ",
            nativeQuery = true)
    List<Venta> obtenerOperaciones(@Param("fechaDesde") String fechaDesde,
                                   @Param("fechaHasta") String fechaHasta,
                                   @Param("sectorId") Long sectorId,
                                   @Param("talDesde") Integer talDesde,
                                   @Param("talHasta") Integer talHasta,
                                   @Param("idFormaPago") Long idFormaPago);

    @Query(value = "SELECT * FROM venta WHERE fecha_comprobante BETWEEN :fechaDesde AND :fechaHasta" +
            " AND (:sectorId IS NULL OR fk_sector = :sectorId) " +
            " AND (fk_talonario NOT BETWEEN :talDesde AND :talHasta) " +
            " AND (:idFormaPago IS NULL OR fk_forma_de_pago >= :idFormaPago) ",
            nativeQuery = true)
    List<Venta> obtenerOperacionesExcluyendoTalonario(
                                    @Param("fechaDesde") String fechaDesde,
                                    @Param("fechaHasta") String fechaHasta,
                                    @Param("sectorId") Long sectorId,
                                    @Param("talDesde") Integer talDesde,
                                    @Param("talHasta") Integer talHasta,
                                    @Param("idFormaPago") Long idFormaPago);

    @Query(value = "SELECT fk_producto FROM venta_detalle WHERE fk_venta = ?", nativeQuery = true)
    List<Long> obtenerProductosVinculados(Long idVenta);
}
