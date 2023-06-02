package com.grupop.gestion.Entidades;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Lote {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descripcion;
    private Double superficieM2;
    private Long nroPlano;
    private Long nroPartida;
    private String ubicacion;
    private String impuestoMun;
    private String impuestoProv;
    @JoinColumn(name = "fk_manzana")
    @ManyToOne
    private Manzana manzana;
    @JoinColumn(name = "fk_urb")
    @ManyToOne
    private Urbanizacion urbanizacion;
    @JoinColumn(name = "fk_estado")
    @ManyToOne
    private EstadoLote estado;
    private boolean esProducto;

}
