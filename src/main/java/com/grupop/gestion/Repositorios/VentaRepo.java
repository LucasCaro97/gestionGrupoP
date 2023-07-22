package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.IndiceCAC;
import com.grupop.gestion.Entidades.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface VentaRepo extends JpaRepository<Venta, Long> {


    @Query( value= "SELECT MAX(id) FROM venta", nativeQuery = true)
    Long findLastId();

    @Query(value = "SELECT total FROM venta WHERE id = ?", nativeQuery = true)
    Double obtenerTotalPorId(Long id);

    @Query(value = "SELECT bloqueada FROM venta WHERE id = ?", nativeQuery = true)
    Boolean validarEstado(Long idVenta);

    @Query(value = "SELECT * FROM venta WHERE bloqueada = 0 AND fk_cliente = ?", nativeQuery = true)
    List<Venta> obtenerVentasSinCreditoPorCliente(Long id);


    @Query(value = "SELECT SUM(total) FROM venta_detalle WHERE fk_venta = ?",nativeQuery = true)
    BigDecimal obtenerTotalProductos(Long id);

    @Query(value = "SELECT SUM(importe) FROM venta_detalle_imputacion WHERE fk_venta = ?", nativeQuery = true)
    BigDecimal obtenerTotalImputacion(Long id);

    @Query(value = "SELECT * FROM venta WHERE fk_cliente = ? AND bloqueada = 0", nativeQuery = true)
    List<Venta> obtenerVentasCtaCtePorCliente(Long id);

    @Query(value = "SELECT fk_indice_base FROM venta WHERE id = ?1", nativeQuery = true)
    Long obtenerIndiceBase(Long idVenta);
}
