package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.Moneda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonedaRepo extends JpaRepository<Moneda, Long> {


}
