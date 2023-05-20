package com.grupop.gestion.DTO;

import com.grupop.gestion.Entidades.Manzana;

public record ManzanaDto(Long id, String descripcion) {

    public ManzanaDto(Manzana manzana){
        this(manzana.getId(), manzana.getDescripcion());
    }

}
