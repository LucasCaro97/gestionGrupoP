package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.Cobro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
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

}
