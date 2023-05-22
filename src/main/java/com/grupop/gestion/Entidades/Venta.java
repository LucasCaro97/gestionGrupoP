package com.grupop.gestion.Entidades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Venta {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "fk_cliente")
    @ManyToOne
    private Cliente cliente;
    private LocalDate fechaComprobante;
    @JoinColumn(name = "fk_tipo_comprobante")
    @ManyToOne
    private TipoComprobante tipoComprobante;
    @JoinColumn(name = "fk_talonario")
    @ManyToOne
    private Talonario talonario;
    private Long nroComprobante;
    @JoinColumn(name = "fk_sector")
    @ManyToOne
    private Sector sector;
    @JoinColumn(name = "fk_moneda")
    @ManyToOne
    private Moneda moneda;
    private String observaciones;

    private BigDecimal subTotal;
    private BigDecimal descuento;
    private BigDecimal importeConImpuesto;
    private BigDecimal importeSinImpuesto;
    private BigDecimal totalImpuesto;
    private BigDecimal total;
    // private Usuario usuario;
    @JoinColumn(name = "fk_forma_de_pago")
    @ManyToOne
    private FormaDePago formaDePago;


}
