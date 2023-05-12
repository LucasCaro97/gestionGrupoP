package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.FormaDePago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FormaDePagoRepo extends JpaRepository<FormaDePago, Long> {


}
