package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.TipoPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoPagoRepo extends JpaRepository<TipoPago, Long> {


}
