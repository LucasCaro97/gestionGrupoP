package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.Lote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoteRepo extends JpaRepository<Lote, Long> {
    @Query(value = "SELECT * FROM lote WHERE fk_urb = ? ORDER BY fk_manzana ASC, nro_lote ASC", nativeQuery = true)
    List<Lote> obtenerPorUrb(Long id);


    @Query(value = "SELECT * FROM lote WHERE fk_urb = ? ORDER BY fk_manzana ASC", nativeQuery = true)
    Page<Lote> searchByUrbanizacion(Long idUrbanizacion, Pageable pageable);

    @Query(value = "SELECT * FROM lote WHERE fk_urb = ?1 AND fk_manzana = ?2 ORDER BY fk_manzana ASC", nativeQuery = true)
    Page<Lote> searchByUrbanizacionAndManzana(Long idUrbanizacion, Long idManzana, Pageable pageable);

    @Query(value = "SELECT COUNT(*) FROM  lote WHERE fk_urb = ?", nativeQuery = true)
    Integer obtenerStockPorUrb(Long idUrb);

    @Query(value = "SELECT COUNT(*) FROM  lote WHERE fk_urb = ? AND fk_estado = 1", nativeQuery = true)
    Integer obtenerDisponiblesPorUrb(Long idUrb);

    @Query(value = "SELECT COUNT(*) FROM  lote WHERE fk_urb = ? AND fk_estado = 3", nativeQuery = true)
    Integer obtenerVendidosPorUrb(Long idUrb);

    Page<Lote> findAllByOrderByUrbanizacionDescripcionAscManzanaDescripcionAscNroLoteAsc(Pageable pageable);



}
