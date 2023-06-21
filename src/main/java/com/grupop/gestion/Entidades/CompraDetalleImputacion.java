package com.grupop.gestion.Entidades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CompraDetalleImputacion {


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "fk_compra")
    @ManyToOne
    private Compra compraId;
    @JoinColumn(name = "fk_cuenta")
    @OneToOne
    private CuentasContables cuentasContables;
    private Double importe;

}
