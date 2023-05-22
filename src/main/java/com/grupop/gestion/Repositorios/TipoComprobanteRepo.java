package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.TipoComprobante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoComprobanteRepo extends JpaRepository<TipoComprobante, Long> {


}
