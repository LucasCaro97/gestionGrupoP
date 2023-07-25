package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.PagoDetalleImputacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagoDetalleImputacionRepo extends JpaRepository<PagoDetalleImputacion, Long> {

    @Query(value = "SELECT COUNT(*) FROM pago_detalle_imputacion WHERE fk_pago = ?1 AND fk_cuenta = ?2", nativeQuery = true)
    Integer existsByPagoAndCuenta(Long idPago, Long idCuenta);

    @Query(value = "SELECT * FROM pago_detalle_imputacion WHERE fk_pago = ?1 AND fk_cuenta = ?2", nativeQuery = true)
    PagoDetalleImputacion searchByPagoAndCuenta(Long idPago, Long idCuenta);

    @Query(value = "SELECT * FROM pago_detalle_imputacion WHERE fk_pago = ?", nativeQuery = true)
    List<PagoDetalleImputacion> obtenerPorPago(Long idPago);


}
