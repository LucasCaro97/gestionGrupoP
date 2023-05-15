package com.grupop.gestion.Entidades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="forma_de_pago")
public class FormaDePago {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descripcion;
    @JoinColumn(name = "fk_tipo_operacion")
    @ManyToOne
    private TipoOperacion tipoOperacion;
    @JoinColumn(name = "fk_tipo_pago")
    @ManyToOne
    private TipoPago tipoPago;
    private String abreviatura;
    private Integer cuenta;
    @JoinColumn(name = "fk_bco_tja")
    @ManyToOne
    private CuentasDeBancoTarjeta bancoTarjeta;
    @JoinColumn(name="fk_moneda")
    @ManyToOne
    private Moneda moneda;
    private Boolean activo;


}
