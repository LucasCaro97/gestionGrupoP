package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.TipoComprobante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TipoComprobanteRepo extends JpaRepository<TipoComprobante, Long> {


    @Query(value = "SELECT * FROM tipo_comprobante WHERE fk_tipo_operacion = ?", nativeQuery = true)
    List<TipoComprobante> searchByTipoComprobante(Long id);
}
