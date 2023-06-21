package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CompraRepo extends JpaRepository<Compra,Long> {

    @Query(value = "SELECT MAX(id) FROM compra", nativeQuery = true)
    Long findLastId();

    @Query(value = "SELECT total FROM compra WHERE id = ?", nativeQuery = true)
    Double obtenerTotalPorId(Long id);


}
