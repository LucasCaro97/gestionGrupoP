package com.grupop.gestion.Entidades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class PlanPago {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descripcion;
    //CARACTERISTICAS DEL PLAN DE PAGO
    private BigDecimal tasaInteresTotal;
    private Integer cantCuota;
    //GASTOS ADMINISTRATIVOS
    private BigDecimal gastosAdministrativos;
    private BigDecimal porcentajeGastos;
    //COBRO FUERA DE TERMINOS
    private BigDecimal diasDeGracia;
    private BigDecimal interesPunitorio;
    //IMPORTE OTORGAMIENTO
    private BigDecimal importeMinimo;
    private BigDecimal importeMaximo;
    //VENCIMIENTO
    private Integer venceLosDias;
    private Boolean tablaCac;


}
