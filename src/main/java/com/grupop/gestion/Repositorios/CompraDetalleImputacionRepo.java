package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.CompraDetalleImputacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompraDetalleImputacionRepo extends JpaRepository<CompraDetalleImputacion, Long> {


    @Query(value = "SELECT COUNT(*) FROM compra_detalle_imputacion WHERE fk_compra = ?1 AND fk_cuenta = ?2", nativeQuery = true)
    Integer existsByCuentaAndCompra(Long idCompra, Long idCuenta);

    @Query(value = "SELECT * FROM compra_detalle_imputacion WHERE fk_compra = ?1 AND fk_cuenta = ?2 ", nativeQuery = true)
    CompraDetalleImputacion seachByCuentaAndCompra(Long idVenta, Long idCuenta);

    @Query(value = "SELECT * FROM compra_detalle_imputacion WHERE fk_compra = ?",nativeQuery = true)
    List<CompraDetalleImputacion> buscarPorCompra(Long id);

}
