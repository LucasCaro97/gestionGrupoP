package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.TipoCuentaBanco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoCuentaBancoRepo extends JpaRepository<TipoCuentaBanco, Long> {

}
