package com.grupop.gestion.Entidades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Venta implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "fk_cliente")
    @ManyToOne
    private Cliente cliente;
    @JoinColumn(name = "fk_tipo_iva")
    @OneToOne
    private TipoIva tipoIva;
    private String cuit;
    private LocalDate fechaComprobante;
    @JoinColumn(name = "fk_tipo_comprobante")
    @ManyToOne
    private TipoComprobante tipoComprobante;
    @JoinColumn(name = "fk_talonario")
    @ManyToOne
    private Talonario talonario;
    private String nroComprobante;
    @JoinColumn(name = "fk_sector")
    @ManyToOne
    private Sector sector;
    @JoinColumn(name = "fk_moneda")
    @ManyToOne
    private Moneda moneda;
    private String observaciones;

    private Double total;
    // private Usuario usuario;
    @JoinColumn(name = "fk_forma_de_pago")
    @ManyToOne
    private FormaDePago formaDePago;

    @OneToMany(mappedBy = "ventaId")
    private List<VentaDetalle> ventaDetalle;
    private boolean ventaCerrada;

}
