package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.Lote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoteRepo extends JpaRepository<Lote, Long> {
    @Query(value = "SELECT * FROM lote WHERE fk_urb = ? ", nativeQuery = true)
    List<Lote> obtenerPorUrb(Long id);


    @Query(value = "SELECT * FROM lote WHERE fk_urb = ?", nativeQuery = true)
    List<Lote> searchByUrbanizacion(Long idUrbanizacion);

    @Query(value = "SELECT * FROM lote WHERE fk_urb = ?1 AND fk_manzana = ?2", nativeQuery = true)
    List<Lote> searchByUrbanizacionAndManzana(Long idUrbanizacion, Long idManzana);
}
