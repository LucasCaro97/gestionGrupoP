package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VentaRepo extends JpaRepository<Venta, Long> {

    @Query( value= "SELECT MAX(id) FROM venta", nativeQuery = true)
    Long findLastId();


}
