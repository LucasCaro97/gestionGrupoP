package com.grupop.gestion.Entidades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="tipo_totalizadora")
public class TipoTotalizadora {


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descripcion;

}
