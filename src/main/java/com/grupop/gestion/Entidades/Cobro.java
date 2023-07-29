package com.grupop.gestion.Entidades;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Cobro {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
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
    private String observaciones;
    @JoinColumn(name = "fk_moneda")
    @ManyToOne
    private Moneda moneda;
    @JoinColumn(name = "fk_forma_de_pago")
    @ManyToOne
    private FormaDePago formaDePago;
    @OneToMany(mappedBy = "cobroId")
    private List<CobroDetalleCuotas> cobroDetalleCuotas;
    @OneToMany(mappedBy = "cobroId")
    private List<CobroDetalleCtaCte> cobroDetalleCtaCte;
    private BigDecimal total;
    @JoinColumn(name = "fk_tipoOp")
    @ManyToOne
    private TipoOperacion tipoOperacion;


}
