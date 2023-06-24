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
public class CompraDetalle {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "fk_compra")
    @ManyToOne
    private Compra compraId;
    @JoinColumn(name = "fk_producto")
    @OneToOne
    private Producto producto;
    @Column(precision = 10, scale = 2)
    private BigDecimal cantidad;
    @Column(precision = 10, scale = 2)
    private BigDecimal precioUnitario;
    @Column(precision = 10, scale = 2)
    private BigDecimal precioFinal;
    @Column(precision = 10, scale = 2)
    private BigDecimal totalsinImpuesto;
    @Column(precision = 10, scale = 2)
    private BigDecimal impuesto;
    @Column(precision = 10, scale = 2)
    private BigDecimal total;




}
