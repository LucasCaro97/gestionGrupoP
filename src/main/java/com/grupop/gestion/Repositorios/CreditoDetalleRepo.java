package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.Credito;
import com.grupop.gestion.Entidades.CreditoDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditoDetalleRepo extends JpaRepository<CreditoDetalle,Long> {


    @Query(value = "SELECT * FROM credito_detalle WHERE fk_credito = ?", nativeQuery = true)
    List<CreditoDetalle> obtenerPorCreditoId(Long idCredito);

    @Query(value = "SELECT * FROM credito_detalle WHERE fk_credito = ?1 AND nro_cuota = ?2", nativeQuery = true)
    CreditoDetalle buscarPorCreditoAndNroCuota(Long creditoId, Integer nroCuota);

    @Query(value = "SELECT * FROM credito_detalle WHERE fk_cliente = ? AND cobrado = 0" , nativeQuery = true)
    List<CreditoDetalle> obtenerPorFkClienteAndEstado(Long id);

    @Query(value = "SELECT MAX(nro_cuota) FROM credito_detalle WHERE fk_credito = ?", nativeQuery = true)
    Integer obtenerUltimoNroCuota(Long idCredito);
}
