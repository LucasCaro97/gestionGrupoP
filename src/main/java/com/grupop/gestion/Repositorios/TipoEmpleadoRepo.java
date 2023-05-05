package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.TipoEmpleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoEmpleadoRepo extends JpaRepository<TipoEmpleado, Long> {


}
