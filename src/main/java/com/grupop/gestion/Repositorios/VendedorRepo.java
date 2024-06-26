package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.Vendedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendedorRepo extends JpaRepository<Vendedor, Long> {

    @Query( value= "SELECT MAX(id_vendedor) FROM vendedor", nativeQuery = true)
    Long findLastId();



}
