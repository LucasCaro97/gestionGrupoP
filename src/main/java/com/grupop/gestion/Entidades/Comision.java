package com.grupop.gestion.Entidades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Comision {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "fk_vendedor")
    @ManyToOne
    private Vendedor vendedor;
    @JoinColumn(name = "fk_venta")
    @OneToOne
    private Venta venta;
    private BigDecimal baseImponible;
    private BigDecimal porcentajeComisionGeneral;
    private BigDecimal comisionGeneral;
    private BigDecimal saldo;

}
