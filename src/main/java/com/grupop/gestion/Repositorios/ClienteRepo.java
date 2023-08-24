package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.Cliente;
import com.grupop.gestion.Entidades.EntidadBase;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ClienteRepo extends JpaRepository<Cliente,Long> {

    @Query( value= "SELECT MAX(id_cliente) FROM cliente", nativeQuery = true)
    Long findLastId();

    @Query(value = "SELECT razon_social FROM entidad_base WHERE fk_cliente = ?", nativeQuery = true)
    String obtenerNombre(Long id);

    @Query(value = "SELECT saldo_a_favor FROM cliente WHERE id_cliente = ? ", nativeQuery = true)
    BigDecimal obtenerSaldoPorIdCliente(Long idCliente);
}
