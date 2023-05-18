package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.Urbanizacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrbanizacionRepo extends JpaRepository<Urbanizacion, Long> {


}
