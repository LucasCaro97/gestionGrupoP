package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.CobroDetalleCuotas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CobroDetalleCuotasRepo extends JpaRepository<CobroDetalleCuotas, Long> {


}
