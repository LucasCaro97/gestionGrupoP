package com.grupop.gestion.Entidades;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Cobro {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    @JoinColumn(name = "fk_cliente")
    @ManyToOne
    private Cliente cliente;
    private LocalDate fechaComprobante;
    @JoinColumn(name = "fk_tipo_comprobante")
    @ManyToOne
    private TipoComprobante tipoComprobante;
    @JoinColumn(name = "fk_talonario")
    @ManyToOne
    private Talonario talonario;
    private String nroComprobante;
    @JoinColumn(name = "fk_sector")
    @ManyToOne
    private Sector sector;
    private String observaciones;
    @OneToMany(mappedBy = "cobroId")
    private List<CobroDetalleCuotas> cobroDetalleCuotas;


}
