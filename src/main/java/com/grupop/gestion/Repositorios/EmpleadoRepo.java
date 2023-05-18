package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmpleadoRepo extends JpaRepository<Empleado, Long> {


    @Query( value= "SELECT MAX(id_empleado) FROM empleado", nativeQuery = true)
    Long findLastId();

    @Query( value= "SELECT * FROM empleado INNER JOIN entidad_base ON empleado.id = entidad_base.fk_empleado", nativeQuery = true)
    List<Empleado> findEmployee();


}
