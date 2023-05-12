package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.TipoOperacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoOperacionRepo extends JpaRepository<TipoOperacion, Long> {


}
