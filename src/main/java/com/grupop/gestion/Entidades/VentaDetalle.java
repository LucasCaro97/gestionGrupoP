package com.grupop.gestion.Entidades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.EnableMBeanExport;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class VentaDetalle implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "fk_venta")
    @ManyToOne()
    private Venta ventaId;
    @JoinColumn(name = "fk_producto")
    @OneToOne
    private Producto producto;
    private Double cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal total;


}
