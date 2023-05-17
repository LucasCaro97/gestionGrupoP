package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.CuentasContables;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CuentasContablesRepo extends JpaRepository<CuentasContables, Long> {


    @Query(value ="SELECT * FROM cuentas_contables WHERE fk_cta_totalizadora = 43;" , nativeQuery = true)
    List<CuentasContables> obtenerCuentas1111();


}
