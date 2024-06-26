package com.grupop.gestion.Entidades;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Venta implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "fk_cliente")
    @ManyToOne
    @JsonIgnore
    private Cliente cliente;
    @JoinColumn(name = "fk_tipo_iva")
    @OneToOne
    @JsonIgnore
    private TipoIva tipoIva;
    private String cuit;
    private LocalDate fechaComprobante;
    @JoinColumn(name = "fk_tipo_comprobante")
    @ManyToOne
    @JsonIgnore
    private TipoComprobante tipoComprobante;
    @Column(name = "fk_talonario")
    private Integer talonario;
    private String nroComprobante;
    @JoinColumn(name = "fk_sector")
    @ManyToOne
    @JsonIgnore
    private Sector sector;
    @JoinColumn(name = "fk_moneda")
    @ManyToOne
    @JsonIgnore
    private Moneda moneda;
    private String observaciones;

    private BigDecimal total;
    // private Usuario usuario;
    @JoinColumn(name = "fk_forma_de_pago")
    @ManyToOne
    @JsonIgnore
    private FormaDePago formaDePago;

    @OneToMany(mappedBy = "ventaId")
    @JsonIgnore
    private List<VentaDetalle> ventaDetalle;
    private boolean bloqueada;
    @JoinColumn(name = "fk_indice_base")
    @ManyToOne
    private IndiceCAC indiceBase;
    @JoinColumn(name = "fk_tipoOp")
    @ManyToOne
    private TipoOperacion tipoOperacion;

}
