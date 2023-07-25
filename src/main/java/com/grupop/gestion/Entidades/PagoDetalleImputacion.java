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
public class PagoDetalleImputacion {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "fk_pago")
    @ManyToOne
    private Pago pagoId;
    @JoinColumn(name = "fk_cuenta")
    @ManyToOne
    private CuentasContables cuenta;
    @Column(precision = 10, scale = 2)
    private BigDecimal importe;




}
