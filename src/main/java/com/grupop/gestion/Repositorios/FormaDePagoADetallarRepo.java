package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.FormaDePagoDetalle;
import com.grupop.gestion.Entidades.FormaDePagoDetallePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;


public interface FormaDePagoADetallarRepo extends JpaRepository<FormaDePagoDetalle, FormaDePagoDetallePK> {

    FormaDePagoDetalle findByIdOperacionAndTipoOperacion(Long idOperacion, Long tipoOperacion);


    @Query(value = "SELECT SUM(monto) FROM forma_de_pago_detalle_sub_detalle WHERE id_operacion = ?1 AND tipo_operacion = ?2", nativeQuery = true)
    BigDecimal obtenerTotal(Long idOperacion, Long idTipoOperacion);

    @Query(value = "SELECT COUNT(*) FROM forma_de_pago_detalle WHERE id_operacion = ?1 AND tipo_operacion = ?2", nativeQuery = true)
    Integer validarExistencia(Long idOperacion, Long idTipoOperacion);


    @Query(value = "SELECT COUNT(*) FROM  forma_de_pago_detalle_sub_detalle WHERE id_operacion = ?1 AND tipo_operacion = ?2", nativeQuery = true)
    Integer validarExistenciaSubDetalle(Long idOperacion, Long idTipoOperacion);

    @Query(value = "SELECT monto FROM forma_de_pago_detalle_sub_detalle WHERE id_operacion = ?1 AND tipo_operacion = ?2 AND fk_forma_de_pago = 3", nativeQuery = true)
    BigDecimal obtenerCapitalCredito(Long idOperacion, Long idTipoOperacion);

    @Query(value = "SELECT bloqueado FROM forma_de_pago_detalle WHERE id_operacion = ?1 AND tipo_operacion = ?2" , nativeQuery = true)
    boolean obtenerEstado(Long idOperacion, Long idTipoOperacion);

    @Modifying
    @Query(value = "DELETE FROM forma_de_pago_detalle WHERE id_operacion = ?1 AND tipo_operacion = ?2", nativeQuery = true)
    void eliminarPorIdOperacionAndIdTipoOperacion(Long id, long l);
}
