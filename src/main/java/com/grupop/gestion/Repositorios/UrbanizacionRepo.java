package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.CuentasContables;
import com.grupop.gestion.Entidades.Urbanizacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UrbanizacionRepo extends JpaRepository<Urbanizacion, Long> {


    @Query(value = "SELECT fk_cuenta FROM urbanizacion WHERE id = ?", nativeQuery = true)
    CuentasContables findCuentaContable(Long id);
}
