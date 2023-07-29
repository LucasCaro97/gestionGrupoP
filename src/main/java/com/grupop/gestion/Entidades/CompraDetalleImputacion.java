package com.grupop.gestion.Entidades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class CompraDetalleImputacion {


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "fk_compra")
    @ManyToOne
    private Compra compraId;
    @JoinColumn(name = "fk_cuenta")
    @OneToOne
    private CuentasContables cuentasContables;
    private BigDecimal importeBase;
    @JoinColumn(name = "fk_impuesto")
    @ManyToMany
    private List<Impuestos> impuestos;
    private BigDecimal importeTotal;


}
