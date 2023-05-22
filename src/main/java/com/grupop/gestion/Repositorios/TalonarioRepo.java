package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.Talonario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TalonarioRepo extends JpaRepository<Talonario, Long> {



}
