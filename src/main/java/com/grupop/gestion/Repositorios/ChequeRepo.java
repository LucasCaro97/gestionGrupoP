package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.Cheque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface ChequeRepo extends JpaRepository<Cheque, Long> {


    @Query(value = "SELECT COUNT(*) FROM cheque WHERE id_operacion = ?", nativeQuery = true)
    Integer validarExistencia(Long id);

    @Query(value = "SELECT SUM(importe) FROM cheque WHERE disponible = 1", nativeQuery = true)
    BigDecimal obtenerTotalDisponible();
}
