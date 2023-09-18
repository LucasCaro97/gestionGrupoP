package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.CobroDetalleCuotas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CobroDetalleCuotasRepo extends JpaRepository<CobroDetalleCuotas, Long> {


    @Query(value = "SELECT * FROM cobro_detalle_cuotas  WHERE cobro_id_id = ?", nativeQuery = true)
    List<CobroDetalleCuotas> obtenerPorCobro(Long id);

    @Modifying
    @Query(value = "DELETE FROM cobro_detalle_cuotas WHERE fk_credito = ?1 AND nro_cuota = ?2 AND cobro_id_id = ?3", nativeQuery = true)
    void eliminarDetalle(Long idCred, Integer nroCuota, Long idCobro);

    @Query( value = "SELECT COUNT(*) FROM cobro_detalle_cuotas WHERE fk_credito = ?1 AND nro_cuota = ?2 AND cobro_id_id = ?3", nativeQuery = true)
    Integer existByCreditoAndNroCuotaAndOperacion(Long idCredito, Integer nroCuota, Long idOperacion);

    @Query(value = "SELECT * FROM cobro_detalle_cuotas WHERE fk_credito = ?1 AND nro_cuota = ?2 AND cobro_id_id = ?3", nativeQuery = true)
    CobroDetalleCuotas searchByCreditoAndNroCuotaAndOperacion(Long idCred, Integer nroCuota, Long idOperacion);
}
