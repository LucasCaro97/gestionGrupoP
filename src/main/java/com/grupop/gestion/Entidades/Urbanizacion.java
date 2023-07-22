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
public class Urbanizacion {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descripcion;
    private Double superficieM2;
    private Long nroPlano;
    private Long nroPartida;
    private String ubicacion;
    private String ciudad;
    @JoinColumn(name = "fk_cuenta")
    @OneToOne
    private CuentasContables cuenta;
    private String linkMapa;


}
