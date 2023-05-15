package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.ContadorTotalizadora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContadorTotalizadoraRepo extends JpaRepository<ContadorTotalizadora, Long> {


}

