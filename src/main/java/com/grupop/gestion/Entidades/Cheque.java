package com.grupop.gestion.Entidades;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Cheque {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate fechaEmision;
    private String serie;
    private Long nroCheque;
    private LocalDate fechaPago;
    @JoinColumn(name = "fk_banco_emisor")
    @ManyToOne
    private BancoEmisor bancoEmisor;
    private Long nroIdentificacion;
    private String titularDeLaCuenta;
    private String ciudad;
    private String provincia;

    private LocalDate fechaRecepcion;
    @JoinColumn(name = "fk_tipo_operacion")
    @ManyToOne
    private TipoOperacion operacionIngreso;
    private Long idOperacion;
    private boolean esTitular;
    private String nombreEmisor;
    private BigDecimal importe;
    private boolean disponible;
    @JoinColumn(name = "fk_tipo_cheque")
    @OneToOne
    private ChequeTipo chequeTipo;
    @JoinColumn(name = "fk_subtipo_cheque")
    @OneToOne
    private ChequeSubtipo chequeSubtipo;



}
