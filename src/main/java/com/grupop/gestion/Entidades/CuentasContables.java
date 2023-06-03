
package com.grupop.gestion.Entidades;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class CuentasContables {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descripcion;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "fk_cta_totalizadora")
    @ManyToOne
    private CuentasTotalizadoras ctaTotalizadora;
    @JoinColumn(name = "fk_clasificacion_cta")
    @OneToOne
    private ClasificacionCta clasificacionCta; //flujo de fondos unico valor
    @JoinColumn(name = "fk_moneda")
    @OneToOne
    @JsonIgnore
    private Moneda moneda;
    private String codigo;
    @JoinColumn(name = "fk_impuesto_cta")
    @ManyToMany
    private List<Impuestos> impuestos;




}