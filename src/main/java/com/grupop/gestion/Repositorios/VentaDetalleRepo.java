package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.VentaDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VentaDetalleRepo extends JpaRepository<VentaDetalle, Long> {


    //VERIFICAR SI NECESITO EL IDVENTA Y EL IDPRODUCTO  || IDVENTA Y DESCPROD || OBJETO VENTA Y OBJETO PRODUCTO

    @Query( value = "SELECT COUNT(*) FROM venta_detalle WHERE fk_venta = ?1 AND fk_producto = ?2", nativeQuery = true)
    Integer existByProductoAndVenta(Long idVenta, Long idProducto);

    @Query(value = "SELECT * FROM venta_detalle WHERE fk_venta = ?1 AND fk_producto = ?2", nativeQuery = true)
    VentaDetalle searchByProductoAndVenta(Long idVenta, Long idProducto);

    @Query(value = "SELECT * FROM venta_detalle WHERE fk_venta = ?", nativeQuery = true)
    List<VentaDetalle> buscarPorVenta(Long id);

    @Query(value = "SELECT SUM(total) FROM venta_detalle WHERE fk_venta = ?", nativeQuery = true)
    Double obtenerTotalPorVenta(Long id);



}
