package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.ChequeSubtipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChequeSubtipoRepo extends JpaRepository<ChequeSubtipo, Long> {



    @Query(value = "SELECT * FROM cheque_subtipo WHERE fk_tipo_cheque = ?", nativeQuery = true)
    List<ChequeSubtipo> findByChequeTipo(Long id);
}
