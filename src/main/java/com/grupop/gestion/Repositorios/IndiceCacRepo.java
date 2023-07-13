package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.IndiceCAC;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface IndiceCacRepo extends JpaRepository<IndiceCAC, Long> {


    @Query(value = "SELECT indice FROM indicecac WHERE mes = ?1 AND anio = ?2", nativeQuery = true)
    BigDecimal obtenerIndiceBase(Integer mes, Integer anio);
}
