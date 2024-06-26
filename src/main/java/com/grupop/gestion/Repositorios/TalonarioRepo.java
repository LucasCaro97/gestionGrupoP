package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.Talonario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TalonarioRepo extends JpaRepository<Talonario, Long> {

    @Query(value = "SELECT ultimo_nro FROM talonario WHERE nro_talonario = ?", nativeQuery = true)
    Long obtenerUltimoNroTalonario(Long id);

    @Query(value = "SELECT id FROM talonario WHERE nro_talonario = ?", nativeQuery = true)
    Long obtenerPorNro(Integer nroTalonario);

    @Query(value = "SELECT * FROM talonario WHERE nro_talonario = ?", nativeQuery = true)
    Talonario obtenerObjetoPorNroTalonario(Integer nroTalonario);
}
