package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.CuentasContables;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CuentasContablesRepo extends JpaRepository<CuentasContables, Long> {


    @Query(value ="SELECT * FROM cuentas_contables WHERE fk_cta_totalizadora = 43;" , nativeQuery = true)
    List<CuentasContables> obtenerCuentas1111();


    @Query(value ="SELECT * FROM cuentas_contables WHERE fk_cta_totalizadora = 44;" , nativeQuery = true)
    List<CuentasContables> obtenerCuentas1112();

    @Query(value ="SELECT * FROM cuentas_contables WHERE fk_cta_totalizadora = 45;" , nativeQuery = true)
    List<CuentasContables> obtenerCuentas1113();

    @Query(value ="SELECT * FROM cuentas_contables WHERE fk_cta_totalizadora = 46;" , nativeQuery = true)
    List<CuentasContables> obtenerCuentas1121();

    @Query(value ="SELECT * FROM cuentas_contables WHERE fk_cta_totalizadora = 47;" , nativeQuery = true)
    List<CuentasContables> obtenerCuentas1122();

    @Query(value ="SELECT * FROM cuentas_contables WHERE fk_cta_totalizadora = 48;" , nativeQuery = true)
    List<CuentasContables> obtenerCuentas1131();
    @Query(value ="SELECT * FROM cuentas_contables WHERE fk_cta_totalizadora = 49;" , nativeQuery = true)
    List<CuentasContables> obtenerCuentas1132();
    @Query(value ="SELECT * FROM cuentas_contables WHERE fk_cta_totalizadora = 50;" , nativeQuery = true)
    List<CuentasContables> obtenerCuentas1133();
    @Query(value ="SELECT * FROM cuentas_contables WHERE fk_cta_totalizadora = 51;" , nativeQuery = true)
    List<CuentasContables> obtenerCuentas1134();
    @Query(value ="SELECT * FROM cuentas_contables WHERE fk_cta_totalizadora = 52;" , nativeQuery = true)
    List<CuentasContables> obtenerCuentas1141();
    @Query(value ="SELECT * FROM cuentas_contables WHERE fk_cta_totalizadora = 53;" , nativeQuery = true)
    List<CuentasContables> obtenerCuentas1142();
    @Query(value ="SELECT * FROM cuentas_contables WHERE fk_cta_totalizadora = 54;" , nativeQuery = true)
    List<CuentasContables> obtenerCuentas1143();
    @Query(value ="SELECT * FROM cuentas_contables WHERE fk_cta_totalizadora = 55;" , nativeQuery = true)
    List<CuentasContables> obtenerCuentas1144();
    @Query(value ="SELECT * FROM cuentas_contables WHERE fk_cta_totalizadora = 59;" , nativeQuery = true)
    List<CuentasContables> obtenerCuentas1211();
    @Query(value ="SELECT * FROM cuentas_contables WHERE fk_cta_totalizadora = 60;" , nativeQuery = true)
    List<CuentasContables> obtenerCuentas1212();
    @Query(value ="SELECT * FROM cuentas_contables WHERE fk_cta_totalizadora = 56;" , nativeQuery = true)
    List<CuentasContables> obtenerCuentas1221();
    @Query(value ="SELECT * FROM cuentas_contables WHERE fk_cta_totalizadora = 57;" , nativeQuery = true)
    List<CuentasContables> obtenerCuentas1222();
    @Query(value ="SELECT * FROM cuentas_contables WHERE fk_cta_totalizadora = 58;" , nativeQuery = true)
    List<CuentasContables> obtenerCuentas1223();
    @Query(value ="SELECT * FROM cuentas_contables WHERE fk_cta_totalizadora = 13;" , nativeQuery = true)
    List<CuentasContables> obtenerCuentas123();
    @Query(value ="SELECT * FROM cuentas_contables WHERE fk_cta_totalizadora = 14;" , nativeQuery = true)
    List<CuentasContables> obtenerCuentas124();
    @Query(value ="SELECT * FROM cuentas_contables WHERE fk_cta_totalizadora = 17;" , nativeQuery = true)
    List<CuentasContables> obtenerCuentas211();
    @Query(value ="SELECT * FROM cuentas_contables WHERE fk_cta_totalizadora = 18;" , nativeQuery = true)
    List<CuentasContables> obtenerCuentas212();
    @Query(value ="SELECT * FROM cuentas_contables WHERE fk_cta_totalizadora = 19;" , nativeQuery = true)
    List<CuentasContables> obtenerCuentas213();
    @Query(value ="SELECT * FROM cuentas_contables WHERE fk_cta_totalizadora = 20;" , nativeQuery = true)
    List<CuentasContables> obtenerCuentas214();
    @Query(value ="SELECT * FROM cuentas_contables WHERE fk_cta_totalizadora = 21;" , nativeQuery = true)
    List<CuentasContables> obtenerCuentas215();
    @Query(value ="SELECT * FROM cuentas_contables WHERE fk_cta_totalizadora = 22;" , nativeQuery = true)
    List<CuentasContables> obtenerCuentas216();
    @Query(value ="SELECT * FROM cuentas_contables WHERE fk_cta_totalizadora = 23;" , nativeQuery = true)
    List<CuentasContables> obtenerCuentas221();
    @Query(value ="SELECT * FROM cuentas_contables WHERE fk_cta_totalizadora = 24;" , nativeQuery = true)
    List<CuentasContables> obtenerCuentas222();
    @Query(value ="SELECT * FROM cuentas_contables WHERE fk_cta_totalizadora = 25;" , nativeQuery = true)
    List<CuentasContables> obtenerCuentas223();
    @Query(value ="SELECT * FROM cuentas_contables WHERE fk_cta_totalizadora = 26;" , nativeQuery = true)
    List<CuentasContables> obtenerCuentas224();
    @Query(value ="SELECT * FROM cuentas_contables WHERE fk_cta_totalizadora = 27;" , nativeQuery = true)
    List<CuentasContables> obtenerCuentas225();
    @Query(value ="SELECT * FROM cuentas_contables WHERE fk_cta_totalizadora = 28;" , nativeQuery = true)
    List<CuentasContables> obtenerCuentas31();
    @Query(value ="SELECT * FROM cuentas_contables WHERE fk_cta_totalizadora = 29;" , nativeQuery = true)
    List<CuentasContables> obtenerCuentas32();
    @Query(value ="SELECT * FROM cuentas_contables WHERE fk_cta_totalizadora = 33;" , nativeQuery = true)
    List<CuentasContables> obtenerCuentas411();
    @Query(value ="SELECT * FROM cuentas_contables WHERE fk_cta_totalizadora = 34;" , nativeQuery = true)
    List<CuentasContables> obtenerCuentas412();
    @Query(value ="SELECT * FROM cuentas_contables WHERE fk_cta_totalizadora = 35;" , nativeQuery = true)
    List<CuentasContables> obtenerCuentas413();
    @Query(value ="SELECT * FROM cuentas_contables WHERE fk_cta_totalizadora = 36;" , nativeQuery = true)
    List<CuentasContables> obtenerCuentas421();
    @Query(value ="SELECT * FROM cuentas_contables WHERE fk_cta_totalizadora = 37;" , nativeQuery = true)
    List<CuentasContables> obtenerCuentas422();
    @Query(value ="SELECT * FROM cuentas_contables WHERE fk_cta_totalizadora = 38;" , nativeQuery = true)
    List<CuentasContables> obtenerCuentas423();
    @Query(value ="SELECT * FROM cuentas_contables WHERE fk_cta_totalizadora = 38;" , nativeQuery = true)
    List<CuentasContables> obtenerCuentas424();
    @Query(value ="SELECT * FROM cuentas_contables WHERE fk_cta_totalizadora = 40;" , nativeQuery = true)
    List<CuentasContables> obtenerCuentas425();
    @Query(value ="SELECT * FROM cuentas_contables WHERE fk_cta_totalizadora = 41;" , nativeQuery = true)
    List<CuentasContables> obtenerCuentas426();
    @Query(value ="SELECT * FROM cuentas_contables WHERE fk_cta_totalizadora = 42;" , nativeQuery = true)
    List<CuentasContables> obtenerCuentas427();
    @Query(value ="SELECT * FROM cuentas_contables WHERE fk_cta_totalizadora = 32;" , nativeQuery = true)
    List<CuentasContables> obtenerCuentas43();





}
