package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.TipoTotalizadora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoTotalizadoraRepo extends JpaRepository<TipoTotalizadora, Long> {



}
