package com.grupop.gestion.Repositorios;


import com.grupop.gestion.Entidades.Manzana;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManzanaRepo extends JpaRepository<Manzana,Long> {
    @Query(value = "SELECT * FROM manzana WHERE fk_urb = ?", nativeQuery = true)
    List<Manzana> obtenerPorUrb(Long id);
}
