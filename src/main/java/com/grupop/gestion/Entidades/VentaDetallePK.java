package com.grupop.gestion.Entidades;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
public class VentaDetallePK implements Serializable {

    private Long ventaId;
    private Integer orderId;

    public VentaDetallePK(){

    }

    public VentaDetallePK(Long facturaId, Integer orderId){

    }


}
