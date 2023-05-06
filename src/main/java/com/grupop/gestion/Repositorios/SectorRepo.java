package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.Sector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectorRepo extends JpaRepository<Sector,Long> {


}
