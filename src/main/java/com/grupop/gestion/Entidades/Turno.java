package com.grupop.gestion.Entidades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Turno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descripcion;
    @Column(name = "hs_entrada")
    private LocalDate hsEntrada;
    @Column(name = "hs_salida")
    private LocalDate hsSalida;
    @Column(name = "hs_entrada_receso")
    private LocalDate hsEntradaReceso;
    @Column(name = "hs_salida_receso")
    private LocalDate hsSalidaReceso;

    private boolean inhabilitado;
}
