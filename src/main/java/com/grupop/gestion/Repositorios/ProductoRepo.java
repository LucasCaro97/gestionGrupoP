package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepo extends JpaRepository<Producto, Long> {



}
