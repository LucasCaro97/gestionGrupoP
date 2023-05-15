package com.grupop.gestion.Entidades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="ctas_bco_tja")
public class CuentasDeBancoTarjeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descripcion;
    private Integer cuenta; //cuenta_contable
    @JoinColumn(name = "fk_tipo_cta_bco")
    @OneToOne
    private TipoCuentaBanco tipoCuentaBanco;
    @JoinColumn(name = "fk_proveedor")
    @OneToOne
    private Proveedor proveedor;
    private Integer cuentaAcreditacion; //cuenta_contable
    private String CBU;
    private String alias;


}
