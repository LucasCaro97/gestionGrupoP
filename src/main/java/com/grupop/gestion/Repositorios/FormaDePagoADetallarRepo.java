package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.FormaDePagoDetalle;
import com.grupop.gestion.Entidades.FormaDePagoDetallePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface FormaDePagoADetallarRepo extends JpaRepository<FormaDePagoDetalle, FormaDePagoDetallePK> {

    FormaDePagoDetalle findByIdOperacionAndTipoOperacion(Long idOperacion, Long tipoOperacion);
}
