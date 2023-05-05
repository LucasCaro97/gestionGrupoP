package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.TipoIva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoIvaRepo extends JpaRepository<TipoIva, Long> {



}

