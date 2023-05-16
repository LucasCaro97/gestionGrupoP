package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.Impuestos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImpuestosRepo extends JpaRepository<Impuestos, Long> {

}
