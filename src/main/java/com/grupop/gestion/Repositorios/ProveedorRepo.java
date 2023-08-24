package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.EntidadBase;
import com.grupop.gestion.Entidades.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProveedorRepo extends JpaRepository<Proveedor,Long> {


    @Query( value= "SELECT MAX(id_proveedor) FROM proveedor", nativeQuery = true)
    Long findLastId();
    @Query(value = "SELECT razon_social FROM entidad_base WHERE fk_proveedor = ?", nativeQuery = true)
    String obtenerNombre(Long id);
    @Query(value = "SELECT saldo_a_favor FROM proveedor WHERE id_proveedor = ? ", nativeQuery = true)
    BigDecimal obtenerSaldoPorIdProveedor(Long idProveedor);

}
