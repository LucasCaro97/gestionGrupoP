package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.Cobro;
import com.grupop.gestion.Entidades.CobroDetalleCuotas;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface CobroRepo extends JpaRepository<Cobro,Long>{


    @Query( value= "SELECT MAX(id) FROM cobro", nativeQuery = true)
    Long buscarUltimoId();

    @Query(value = "SELECT SUM(importe_final) FROM cobro_detalle_cuotas WHERE cobro_id_id = ?",nativeQuery = true)
    Optional<BigDecimal> obtenerTotalCuotas(Long idCobro);

    @Query(value = "SELECT total FROM cobro WHERE id = ?", nativeQuery = true)
    BigDecimal obtenerTotalPorId(Long id);

    @Query(value = "SELECT SUM(total_detalle) FROM cobro_detalle_cta_cte WHERE cobro_id_id = ?",nativeQuery = true)
    Optional<BigDecimal> obtenerTotalCtaCte(Long idCobro);

    @Query(value = "SELECT SUM(total) FROM cobro WHERE fk_moneda = 1 AND DATE_FORMAT(fecha_comprobante, '%Y-%m') = DATE_FORMAT(CURRENT_DATE(), '%Y-%m')", nativeQuery = true)
    BigDecimal obtenerTotalCobradoMensual();

    Cobro findTopByOrderByIdDesc();
    @Query(value = "SELECT SUM(importe) FROM cobro_detalle_adelanto WHERE cobro_id_id = ?", nativeQuery = true)
    Optional<BigDecimal> obtenerTotalAdelanto(Long idCobro);

    @Query(value = "SELECT fk_cliente FROM cobro WHERE id = ?" , nativeQuery = true)
    Long obtenerCliente(Long idOperacion);

    Page<Cobro> findAllByOrderByIdDesc(Pageable pageable);


    @Query(value = "SELECT * FROM cobro WHERE fecha_comprobante BETWEEN :fechaDesde AND :fechaHasta" +
            " AND (:sectorId IS NULL OR fk_sector = :sectorId) " +
            " AND (:talDesde IS NULL OR fk_talonario >= :talDesde) " +
            " AND (:talHasta IS NULL OR fk_talonario <= :talHasta) " +
            " AND (:idFormaPago IS NULL OR fk_forma_de_pago >= :idFormaPago) ",
            nativeQuery = true)
    List<Cobro> obtenerOperaciones(String fechaDesde, String fechaHasta, Long sectorId, Integer talDesde, Integer talHasta, Long idFormaPago);


    @Query(value = "SELECT * FROM cobro WHERE fecha_comprobante BETWEEN :fechaDesde AND :fechaHasta" +
            " AND (:sectorId IS NULL OR fk_sector = :sectorId) " +
            " AND (fk_talonario NOT BETWEEN :talDesde AND :talHasta) " +
            " AND (:idFormaPago IS NULL OR fk_forma_de_pago >= :idFormaPago) ",
            nativeQuery = true)
    List<Cobro> obtenerOperacionesExcluyendoTalonario(@Param("fechaDesde") String fechaDesde,
                                                      @Param("fechaHasta") String fechaHasta,
                                                      @Param("sectorId") Long sectorId,
                                                      @Param("talDesde") Integer talDesde,
                                                      @Param("talHasta") Integer talHasta,
                                                      @Param("idFormaPago") Long idFormaPago);


    @Query( value = "SELECT * FROM cobro_detalle_cuotas WHERE cobro_id_id = ?" , nativeQuery = true)
    List<CobroDetalleCuotas> obtenerLineasDetalle(Long idCobro);
}
