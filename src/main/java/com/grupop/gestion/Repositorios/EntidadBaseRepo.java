package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.EntidadBase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntidadBaseRepo extends JpaRepository<EntidadBase,Long> {


}
