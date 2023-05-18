package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.Lote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoteRepo extends JpaRepository<Lote, Long> {
}
