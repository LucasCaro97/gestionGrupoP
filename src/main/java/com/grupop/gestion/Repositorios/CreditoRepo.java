package com.grupop.gestion.Repositorios;

import com.grupop.gestion.Entidades.Credito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditoRepo extends JpaRepository<Credito, Long> {

    @Query( value = "SELECT COUNT(*) FROM credito WHERE fk_venta = ?", nativeQuery = true)
    Integer existByIdVenta(Long idVenta);

    Credito findFirstByOrderByIdDesc();

    @Query(value = "SELECT MAX(id) FROM credito", nativeQuery = true)
    Long buscarUltimoId();

    @Query(value = "SELECT bloqueado FROM credito WHERE id = ?", nativeQuery = true)
    boolean validarEstado(Long idCred);


    Page<Credito> findAllByOrderByIdDesc(Pageable pageable);
}
