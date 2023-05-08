package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepo extends JpaRepository<Cliente,Long> {

    @Query( value= "SELECT MAX(id) FROM cliente", nativeQuery = true)
    Long findLastId();


}
