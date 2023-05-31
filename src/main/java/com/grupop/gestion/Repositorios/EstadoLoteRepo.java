package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.EstadoLote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoLoteRepo extends JpaRepository<EstadoLote,Long> {
}
