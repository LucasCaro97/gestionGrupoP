package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.VentaDetalleImputacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VentaDetalleImputacionRepo extends JpaRepository<VentaDetalleImputacion, Long> {

    @Query(value = "SELECT COUNT(*) FROM venta_detalle_imputacion WHERE fk_venta = ?1 AND fk_cuenta = ?2", nativeQuery = true)
    Integer existByCuentaAndVenta(Long idVenta, Long idCuenta);

    @Query(value = "SELECT * FROM venta_detalle_imputacion WHERE fk_venta = ?1 AND fk_producto = ?2", nativeQuery = true)
    VentaDetalleImputacion searchByCuentaAndVenta(Long idVenta, Long idCuenta);

    @Query(value = "SELECT * FROM venta_detalle_imputacion WHERE fk_venta = ?", nativeQuery = true)
    List<VentaDetalleImputacion> buscarPorVenta(Long id);


}
