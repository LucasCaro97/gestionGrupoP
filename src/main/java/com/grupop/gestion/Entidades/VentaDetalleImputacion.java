package com.grupop.gestion.Entidades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class VentaDetalleImputacion {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "fk_venta")
    @ManyToOne()
    private Venta ventaId;
    @JoinColumn(name = "fk_cuenta")
    @OneToOne
    private CuentasContables cuentasContables;
    private Double importe;

}
