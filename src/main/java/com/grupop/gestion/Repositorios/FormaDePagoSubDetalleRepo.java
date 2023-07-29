package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.FormaDePagoDetalleSubDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormaDePagoSubDetalleRepo extends JpaRepository<FormaDePagoDetalleSubDetalle,Long> {


    @Query(value = "SELECT * FROM forma_de_pago_detalle_sub_detalle WHERE id_operacion = ?1 AND tipo_operacion = ?2", nativeQuery = true)
    List<FormaDePagoDetalleSubDetalle> obtenerPorMaestro(Long idOperacion, Long idTipoOperacion);

}
