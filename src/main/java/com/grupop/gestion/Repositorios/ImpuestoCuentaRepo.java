package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.ImpuestoCuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImpuestoCuentaRepo extends JpaRepository<ImpuestoCuenta, Long> {

}
