package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.CreditoDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditoDetalleRepo extends JpaRepository<CreditoDetalle,Long> {


}