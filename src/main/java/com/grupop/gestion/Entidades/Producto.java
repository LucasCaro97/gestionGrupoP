package com.grupop.gestion.Entidades;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Producto {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long id;
    private String descripcion;
    @JoinColumn(name = "fk_cuentasContables")
    @OneToOne
    private CuentasContables cuentasContables;
    @JoinColumn(name = "fk_tipo_prod")
    @OneToOne
    @JsonIgnore
    private TipoProducto tipoProducto;
    @ManyToMany
    private List<Impuestos> impuestos;
    @JoinColumn(name = "fk_lote")
    @OneToOne
    @JsonIgnore
    private Lote lote;


}
