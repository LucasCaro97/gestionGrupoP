package com.grupop.gestion.Entidades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "cuentas_totalizadoras")
public class CuentasTotalizadoras {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descripcion;
    @JoinColumn(name = "fk_tipo_totalizadora")
    @OneToOne
    private TipoTotalizadora tipoTotalizadora;
    private Integer padre;
    private String nomenclatura;
    @JoinColumn(name = "fk_nivel")
    @OneToOne
    private ContadorTotalizadora nivel;
    private Integer nroCta;
}
