package com.grupop.gestion.Entidades;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="forma_de_pago")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class FormaDePago {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descripcion;
    @JoinColumn(name = "fk_tipo_operacion")
    @ManyToOne
    @JsonIgnore
    private TipoOperacion tipoOperacion;
    @JoinColumn(name = "fk_tipo_pago")
    @ManyToOne
    @JsonIgnore
    private TipoPago tipoPago;
    private String abreviatura;
    @JoinColumn(name = "fk_cuenta")
    @OneToOne
    @JsonIgnore
    private CuentasContables cuenta;
    @JoinColumn(name = "fk_bco_tja")
    @ManyToOne
    @JsonIgnore
    private CuentasDeBancoTarjeta bancoTarjeta;
    @JoinColumn(name="fk_moneda")
    @ManyToOne
    @JsonIgnore
    private Moneda moneda;
    private Boolean activo;


}
