package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.CreditoDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditoDetalleRepo extends JpaRepository<CreditoDetalle,Long> {


    @Query(value = "SELECT * FROM credito_detalle WHERE fk_credito = ?", nativeQuery = true)
    List<CreditoDetalle> obtenerPorCreditoId(Long idCredito);
}
