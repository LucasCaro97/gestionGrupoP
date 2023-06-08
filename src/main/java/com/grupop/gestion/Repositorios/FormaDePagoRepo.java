package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.FormaDePago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormaDePagoRepo extends JpaRepository<FormaDePago, Long> {


    @Query(value = "SELECT * FROM forma_de_pago WHERE fk_tipo_operacion = ?", nativeQuery = true)
    List<FormaDePago> obtenerTodosPorOperacion(Long idOperacion);
}
