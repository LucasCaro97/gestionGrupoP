package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.Cobro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CobroRepo extends JpaRepository<Cobro,Long>{


}
