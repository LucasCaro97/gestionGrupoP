package com.grupop.gestion.Repositorios;

import com.grupop.gestion.DTO.CobroCuotasDTO;
import com.grupop.gestion.Entidades.Credito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CreditoRepo extends JpaRepository<Credito, Long> {

    @Query( value = "SELECT COUNT(*) FROM credito WHERE fk_venta = ?", nativeQuery = true)
    Integer existByIdVenta(Long idVenta);

    Credito findFirstByOrderByIdDesc();

    @Query(value = "SELECT MAX(id) FROM credito", nativeQuery = true)
    Long buscarUltimoId();

    @Query(value = "SELECT bloqueado FROM credito WHERE id = ?", nativeQuery = true)
    boolean validarEstado(Long idCred);


    Page<Credito> findAllByOrderByIdDesc(Pageable pageable);


    @Query(value = "SELECT refinancia FROM credito WHERE id = ?", nativeQuery = true)
    BigDecimal obtenerImporteRefinancia(Long id);

    @Query( value = "SELECT COUNT(*) FROM credito WHERE fk_venta = ? AND bloqueado = 0 " , nativeQuery = true)
    Integer verificarSiHayUnCreditoActivoPorFkVenta(Long id);


    @Query( value = "SELECT cobro.id, cobro.fk_talonario , cobro.fk_cliente , cobro_detalle_cuotas.fk_credito, cobro_detalle_cuotas.nro_cuota, credito.fk_plan_pago , cobro.fecha_comprobante, " +
            " cobro_detalle_cuotas.importe_cuota, cobro_detalle_cuotas.importe_ajuste, cobro_detalle_cuotas.importe_intereses, cobro_detalle_cuotas.importe_bonificacion, cobro_detalle_cuotas.importeacobrar, cobro_detalle_cuotas.cancelo " +
            " FROM cobro " +
            " INNER JOIN cobro_detalle_cuotas ON cobro.id = cobro_detalle_cuotas.cobro_id_id" +
            " INNER JOIN credito ON cobro_detalle_cuotas.fk_credito = credito.id" +
            " WHERE ( :idCliente IS NULL OR cobro.fk_cliente = :idCliente )" +
            "AND ( :fechaDesde IS NULL OR cobro.fecha_comprobante >= :fechaDesde )" +
            "AND ( :fechaHasta IS NULL OR cobro.fecha_comprobante <= :fechaHasta )" +
            "AND ( COALESCE(:planPago) IS NULL OR credito.fk_plan_pago IN (:planPago) )  ", nativeQuery = true)
    List<Object[]> obtenerRegistrosReporte(Long idCliente, String fechaDesde, String fechaHasta, List<String> planPago);

}
