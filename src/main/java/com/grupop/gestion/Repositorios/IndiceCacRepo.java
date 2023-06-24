package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.IndiceCAC;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndiceCacRepo extends JpaRepository<IndiceCAC, Long> {


}
