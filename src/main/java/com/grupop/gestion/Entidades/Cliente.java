package com.grupop.gestion.Entidades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Long id;
    @Column(name = "fecha_alta")
    private LocalDate fechaAlta = LocalDate.now();
    @Column(name = "saldo_a_favor")
    private BigDecimal saldoAFavor;


    public String toString() {
        return this.getId().toString();
    }
}
