package com.grupop.gestion.Entidades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "fk_proveedor")
    @ManyToOne
    private Proveedor proveedor;
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
    @Column(precision = 10, scale = 2)
    private BigDecimal total;
    //private Usuario usuario;
    @JoinColumn(name = "fk_forma_de_pago")
    @ManyToOne
    private FormaDePago formaDePago;
    @OneToMany(mappedBy = "pagoId")
    private List<PagoDetalle> PagoDetalle;



}
