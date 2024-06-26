package com.grupop.gestion.Entidades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter

public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empleado")
    private Long id;
    @JoinColumn(name = "fk_tipo_empleado")
    @OneToOne
    private TipoEmpleado tipoEmpleado;
    @JoinColumn(name = "fk_puesto")
    @OneToOne
    private Puesto puesto;
    @JoinColumn(name = "fk_sector")
    @OneToOne
    private Sector sector;
    @JoinColumn(name = "fk_turno")
    @OneToOne
    private Turno turno;
    @JoinColumn(name = "fk_departamento")
    @OneToOne
    private Departamento departamento;
    @Column(name = "fecha_alta")
    private LocalDate fechaAlta = LocalDate.now();


    public String toString() {
        return this.getId().toString();
    }
}
