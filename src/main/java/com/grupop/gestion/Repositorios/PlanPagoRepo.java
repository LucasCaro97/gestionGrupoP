package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.PlanPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanPagoRepo extends JpaRepository<PlanPago, Long> {



}

