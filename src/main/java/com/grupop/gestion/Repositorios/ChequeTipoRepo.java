package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.ChequeTipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChequeTipoRepo extends JpaRepository<ChequeTipo, Long> {


}
