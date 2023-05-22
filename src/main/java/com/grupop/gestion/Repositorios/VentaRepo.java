package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VentaRepo extends JpaRepository<Venta, Long> {




}
