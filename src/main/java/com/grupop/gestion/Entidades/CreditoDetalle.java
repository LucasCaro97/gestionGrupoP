package com.grupop.gestion.Entidades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class CreditoDetalle {


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "fk_credito")
    @ManyToOne
    private Credito creditoId;
    @JoinColumn(name = "fk_cliente")
    @ManyToOne
    private Cliente cliente;
    private Integer nroCuota;
    private BigDecimal capital;
    private BigDecimal gastoAdm;
    private BigDecimal monto;
    private LocalDate vencimiento;
    private BigDecimal saldo;
    private Boolean cobrado;



}
