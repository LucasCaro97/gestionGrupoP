package com.grupop.gestion.Repositorios;


import com.grupop.gestion.Entidades.Manzana;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManzanaRepo extends JpaRepository<Manzana,Long> {
}
