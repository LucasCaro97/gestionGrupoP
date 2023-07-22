package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.CobroDetalleCtaCte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CobroDetalleCtaCteRepo extends JpaRepository<CobroDetalleCtaCte, Long> {


    @Query(value = "SELECT * FROM cobro_detalle_cta_cte WHERE cobro_id_id = ?", nativeQuery = true)
    List<CobroDetalleCtaCte> obtenerPorCobro(Long id);

    @Modifying
    @Query(value = "DELETE FROM cobro_detalle_cta_cte WHERE cobro_id_id = ?1 AND fk_venta = ?2 AND total_detalle = ?3", nativeQuery = true)
    void deleteByCobroAndVentaAndImporte(Long idCobro, Long idVenta, BigDecimal importe);

    @Query(value = "SELECT COUNT(*) FROM cobro_detalle_cta_cte WHERE fk_venta = ?1", nativeQuery = true)
    Integer existByVentaId(Long idVenta);


    @Query(value = "SELECT * FROM cobro_detalle_cta_cte WHERE fk_venta = ?1", nativeQuery = true)
    CobroDetalleCtaCte searchByVentaId(Long idVenta);
}
