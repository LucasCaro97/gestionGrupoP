package com.grupop.gestion.Entidades;

import com.grupop.gestion.Repositorios.CobroDetalleAdelantoRepo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CobroDetalleAdelanto {


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Cobro cobroId;
    @ManyToOne
    private Cliente cliente;
    private String detalle;
    private BigDecimal importe;


}
