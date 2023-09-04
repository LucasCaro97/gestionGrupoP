package com.grupop.gestion.Entidades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Manzana {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descripcion;
    @JoinColumn(name = "fk_urb")
    @ManyToOne
    private Urbanizacion urbanizacion;
    private Integer ultimoNroLote;



    @Override
    public String toString() {
        return descripcion;
    }
}
