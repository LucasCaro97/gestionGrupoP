package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.Credito;
import com.grupop.gestion.Entidades.CreditoDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface CreditoDetalleRepo extends JpaRepository<CreditoDetalle,Long> {


    @Query(value = "SELECT * FROM credito_detalle WHERE fk_credito = ?", nativeQuery = true)
    List<CreditoDetalle> obtenerPorCreditoId(Long idCredito);

    @Query(value = "SELECT * FROM credito_detalle WHERE fk_credito = ?1 AND nro_cuota = ?2", nativeQuery = true)
    CreditoDetalle buscarPorCreditoAndNroCuota(Long creditoId, Integer nroCuota);

    @Query(value = "SELECT * FROM credito_detalle WHERE fk_cliente = ? AND saldo > 0 AND estado_cuota = 1" , nativeQuery = true)
    List<CreditoDetalle> obtenerPorFkClienteAndEstadoActivo(Long id);

    @Query(value = "SELECT MAX(nro_cuota) FROM credito_detalle WHERE fk_credito = ?", nativeQuery = true)
    Integer obtenerUltimoNroCuota(Long idCredito);


    @Query(value = "SELECT * FROM credito_detalle WHERE vencimiento < LAST_DAY(CURDATE()) AND saldo > 0", nativeQuery = true)
    List<CreditoDetalle> obtenerCuotasCobrarMensual();

    @Query(value = "SELECT vencimiento FROM credito_detalle WHERE fk_credito = ? LIMIT 1;", nativeQuery = true)
    LocalDate obtenerFechaPrimerVencimiento(Long idCredito);

    @Query(value = "SELECT SUM(saldo) FROM credito_detalle WHERE fk_credito = ?", nativeQuery = true)
    BigDecimal verificarSaldoByIdCredito(Long creditoId);

    @Query(value = "SELECT * FROM credito_detalle WHERE fk_credito = ?1 AND nro_cuota = ?2"  , nativeQuery = true)
    CreditoDetalle obtenerPorIdCreditoAndNroCuota(Long idCred, Integer nroCuota);
}
