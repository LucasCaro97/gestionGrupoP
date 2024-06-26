package com.grupop.gestion.Entidades;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@IdClass(FormaDePagoDetallePK.class)
@ToString
public class FormaDePagoDetalle {

    @Id
    private Long idOperacion ;
    @Id
    private Long tipoOperacion;

    private String observacion;

    private BigDecimal totalOperacion;
    private boolean bloqueado;


}
