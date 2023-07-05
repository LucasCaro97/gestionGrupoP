package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.Comision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComisionRepo extends JpaRepository<Comision, Long> {


    @Query(value = "SELECT * FROM comision WHERE fk_venta = ?", nativeQuery = true)
    List<Comision> obtenerComisionVenta(Long idVenta);
}
