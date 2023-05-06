package com.grupop.gestion.Entidades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Getter
@Setter
public class Turno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descripcion;
    @Column(name = "hs_entrada")
    private LocalTime hsEntrada;
    @Column(name = "hs_salida")
    private LocalTime hsSalida;
    @Column(name = "hs_entrada_receso")
    private LocalTime hsEntradaReceso;
    @Column(name = "hs_salida_receso")
    private LocalTime hsSalidaReceso;

    private boolean inhabilitado = false;
}
