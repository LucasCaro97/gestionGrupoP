package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.Lote;
import com.grupop.gestion.Entidades.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
    Page<Producto> searchByTipoProdAndCuenta(Long idTipoProd, Long idCuenta, Pageable pageable);

    @Query(value ="SELECT * FROM producto WHERE fk_tipo_prod = ?1 AND fk_cuentas_contables = ?2 AND descripcion LIKE %?3%", nativeQuery = true)
    Page<Producto> searchByTipoProdAndCuentaAndDesc(Long idTipoProd, Long idCuenta, String descripcion, Pageable pageable);


    @Query(value = "SELECT * FROM producto WHERE fk_tipo_prod = ?", nativeQuery = true)
    Page<Producto> searchByTipoProd(Long idTipoProd, Pageable pageable);

    @Query(value = "SELECT * FROM producto WHERE fk_tipo_prod = ?1 AND descripcion LIKE %?2%", nativeQuery = true)
    Page<Producto> searchByTipoProdAndDescripcion(Long idTipoProd, String descripcion, Pageable pageable);


    @Query(value = "SELECT * FROM producto WHERE fk_cuentas_contables = ? AND estado = 1", nativeQuery = true)
    List<Producto> searchByCuenta(Long idCuenta);
    @Query(value = "SELECT * FROM producto WHERE fk_cuentas_contables = ?1 AND descripcion LIKE %?2% AND estado = 1", nativeQuery = true)
    List<Producto> searchByCuentaAndDesc(Long idCuenta, String descripcion);

    @Query(value = "SELECT * FROM producto WHERE descripcion LIKE %?% AND estado = 1", nativeQuery = true)
    List<Producto> searchByDescripcion(String descripcion);

    @Query(value = "SELECT * FROM producto WHERE descripcion LIKE %?1%", nativeQuery = true)
    Producto searchProductoByDescripcion(String descripcion);

    @Query(value = "SELECT * FROM producto WHERE estado = ? AND fk_tipo_prod = 1", nativeQuery = true)
    List<Producto> searchByEstadoAndTipoProd(Boolean estado);
    @Query(value = "SELECT * FROM producto WHERE fk_cuentas_contables = ? AND estado = 1", nativeQuery = true)
    Page<Producto> searchByCuentaPageable(Long idCuenta, Pageable pageable);
    @Query(value = "SELECT * FROM producto WHERE fk_cuentas_contables = ?1 AND descripcion LIKE %?2% AND estado = 1", nativeQuery = true)
    Page<Producto> searchByCuentaAndDescPageable(Long idCuenta, String descripcion, Pageable pageable);

    @Query(value = "SELECT * FROM producto WHERE descripcion LIKE %?1%", nativeQuery = true)
    Page<Producto> searchByDescripcionPageable(String descripcion, PageRequest of);

    @Query(value = "SELECT * FROM producto", nativeQuery = true)
    Page<Producto> findAllPageable(Pageable pageable);

    @Query(value = "SELECT * FROM producto WHERE estado = ? AND fk_tipo_prod <> 1", nativeQuery = true)
    List<Producto> searchByEstadoAndCategoria(Boolean estado);
}
