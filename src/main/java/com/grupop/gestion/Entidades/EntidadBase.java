package com.grupop.gestion.Entidades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class EntidadBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "razon_social")
    private String razonSocial;

    private Long cuit;
    @Column(name = "fecha_alta")
    private LocalDate fechaAlta = LocalDate.now();
    @Column(name="nombre_fantasia")
    private String nombreFantasia;
    @JoinColumn(name = "fk_tipo_iva")
    @OneToOne
    private TipoIva tipoIva;
    @JoinColumn(name = "fk_cliente")
    @OneToOne
    private Cliente cliente;
    @JoinColumn(name = "fk_proveedor")
    @OneToOne
    private Proveedor proveedor;
    @JoinColumn(name = "fk_empleado")
    @OneToOne
    private Empleado empleado;
    @JoinColumn(name = "fk_vendedor")
    @OneToOne
    private Vendedor vendedor;
    private String domicilio;
    private Long telefono;
    private String correo;
    @Column(name = "fecha_modificacion")
    private LocalDate fechaModificacion = LocalDate.now();
}
