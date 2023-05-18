package com.grupop.gestion.Entidades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proveedor")
    private Long id;
    @Column(name = "fecha_alta")
    private LocalDate fechaAlta = LocalDate.now();


    public String toString() {
        return this.getId().toString();
    }
}
