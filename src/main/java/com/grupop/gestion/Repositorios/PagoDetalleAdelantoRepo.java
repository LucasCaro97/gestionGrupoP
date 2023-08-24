package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.CobroDetalleAdelanto;
import com.grupop.gestion.Entidades.PagoDetalleAdelanto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagoDetalleAdelantoRepo extends JpaRepository<PagoDetalleAdelanto, Long> {


    @Query(value = "SELECT * FROM pago_detalle_adelanto WHERE pago_id_id = ?", nativeQuery = true)
    List<PagoDetalleAdelanto> obtenerPorPago(Long id);




}
