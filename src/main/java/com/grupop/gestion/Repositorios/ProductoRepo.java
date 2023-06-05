package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.Lote;
import com.grupop.gestion.Entidades.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepo extends JpaRepository<Producto, Long> {

    boolean existsByLote(Lote lote);

    @Query(value = "UPDATE lote SET es_producto = 0 WHERE id > 0", nativeQuery = true)
    boolean bajaProductoLote();

    @Query(value ="SELECT * FROM producto WHERE fk_tipo_prod = ?1 AND fk_cuentas_contables = ?2", nativeQuery = true)
    List<Producto> searchByTipoProdAndCuenta(Long idTipoProd, Long idCuenta);

    @Query(value ="SELECT * FROM producto WHERE fk_tipo_prod = ?1 AND fk_cuentas_contables = ?2 AND descripcion LIKE %?3%", nativeQuery = true)
    List<Producto> searchByTipoProdAndCuentaAndDesc(Long idTipoProd, Long idCuenta, String descripcion);


    @Query(value = "SELECT * FROM producto WHERE fk_tipo_prod = ?", nativeQuery = true)
    List<Producto> searchByTipoProd(Long idTipoProd);

    @Query(value = "SELECT * FROM producto WHERE fk_tipo_prod = ?1 AND descripcion LIKE %?2%", nativeQuery = true)
    List<Producto> searchByTipoProd(Long idTipoProd, String descripcion);


    @Query(value = "SELECT * FROM producto WHERE fk_cuentas_contables = ?", nativeQuery = true)
    List<Producto> searchByCuenta(Long idCuenta);
    @Query(value = "SELECT * FROM producto WHERE fk_cuentas_contables = ?1 AND descripcion LIKE %?2%", nativeQuery = true)
    List<Producto> searchByCuentaAndDesc(Long idCuenta, String descripcion);

    @Query(value = "SELECT * FROM producto WHERE descripcion LIKE %?% ", nativeQuery = true)
    List<Producto> searchByDescripcion(String descripcion);

}
