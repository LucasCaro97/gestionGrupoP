package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.Puesto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PuestoRepo extends JpaRepository<Puesto, Long> {


}
