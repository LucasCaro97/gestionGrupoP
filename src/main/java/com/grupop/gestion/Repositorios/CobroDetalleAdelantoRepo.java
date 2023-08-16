package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.CobroDetalleAdelanto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CobroDetalleAdelantoRepo extends JpaRepository<CobroDetalleAdelanto, Long> {


    @Query(value = "SELECT * FROM cobro_detalle_adelanto WHERE cobro_id_id = ?", nativeQuery = true)
    List<CobroDetalleAdelanto> obtenerPorCobro(Long id);

}
