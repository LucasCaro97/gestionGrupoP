package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpleadoRepo extends JpaRepository<Empleado, Long> {


    @Query( value= "SELECT MAX(id) FROM empleado", nativeQuery = true)
    Long findLastId();


}
