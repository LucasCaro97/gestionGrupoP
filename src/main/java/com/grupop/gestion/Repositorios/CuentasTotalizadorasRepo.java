package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.CuentasTotalizadoras;
import com.sun.jdi.LongValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface CuentasTotalizadorasRepo extends JpaRepository<CuentasTotalizadoras, Long> {

    @Query(value = "SELECT * FROM cuentas_totalizadoras WHERE id = (SELECT MAX(id) FROM cuentas_totalizadoras)" , nativeQuery = true)
    CuentasTotalizadoras obtenerUltimoRegistro();

    @Query( value= "SELECT MAX(id) FROM proveedor", nativeQuery = true)
    Long findLastId();

    @Query(value = "SELECT nomenclatura FROM cuentas_totalizadoras WHERE id = ?", nativeQuery = true)
    String obtenerNomenclaturaPadre(Integer padre);
}
