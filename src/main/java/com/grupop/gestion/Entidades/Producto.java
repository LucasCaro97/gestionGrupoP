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
public class Producto {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long id;
    private String descripcion;
    private Integer rubroFamilia;
    @JoinColumn(name = "fk_cuentasContables")
    @OneToOne
    private CuentasContables cuentasContables;
    @JoinColumn(name = "fk_tipo_prod")
    @OneToOne
    private TipoProducto tipoProducto;
    @ManyToMany
    private List<Impuestos> impuestos;
    private Integer DatosTerreno;
    private Integer DatosLote;

}
