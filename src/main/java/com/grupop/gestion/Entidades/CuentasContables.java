
package com.grupop.gestion.Entidades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CuentasContables {

    @Id @GeneratedValue
    private Long id;
    private String descripcion;
    @JoinColumn(name = "fk_cta_totalizadora")
    @OneToOne
    private CuentasTotalizadoras ctaTotalizadora;
    @JoinColumn(name = "fk_clasificacion_cta")
    @OneToOne
    private ClasificacionCta clasificacionCta; //flujo de fondos unico valor
    @JoinColumn(name = "fk_moneda")
    @OneToOne
    private Moneda moneda;
    private String codigo;
    @JoinColumn(name = "fk_impuesto_cta")
    @OneToMany
    private List<Impuestos> impuestos;




}