package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.BancoEmisor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BancoEmisorRepo extends JpaRepository<BancoEmisor, Long> {



}
