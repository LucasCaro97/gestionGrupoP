package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.CuentasDeBancoTarjeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CuentasDeBancoTarjetaRepo extends JpaRepository<CuentasDeBancoTarjeta, Long> {


}
