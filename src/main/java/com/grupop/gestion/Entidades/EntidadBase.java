package com.grupop.gestion.Entidades;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class EntidadBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_entidad")
    private Long id;
    private Long idAnt;
    @Column(name = "razon_social")
    private String razonSocial;

    private Long cuit;
    @Column(name = "fecha_alta")
    private LocalDate fechaAlta = LocalDate.now();
    @Column(name="nombre_fantasia")
    private String nombreFantasia;
    @JoinColumn(name = "fk_tipo_iva")
    @OneToOne
    @JsonIgnore
    private TipoIva tipoIva;
    @JoinColumn(name = "fk_cliente")
    @OneToOne
    @JsonIgnore
    private Cliente cliente;
    @JoinColumn(name = "fk_proveedor")
    @OneToOne
    @JsonIgnore
    private Proveedor proveedor;
    @JoinColumn(name = "fk_empleado")
    @OneToOne
    @JsonIgnore
    private Empleado empleado;
    @JoinColumn(name = "fk_vendedor")
    @OneToOne
    @JsonIgnore
    private Vendedor vendedor;
    private String domicilio;
    private Long telefono;
    private String correo;
    @Column(name = "fecha_modificacion")
    private LocalDate fechaModificacion = LocalDate.now();

    //AGREGAR CAMPOS DE DATOS FILIATORIOS EN CASO DE SER NECESARIO
//    fechaNacimiento
//    estadoCivil
//    cantidadHijos
//    grupoSanguineo
//    sexo
}
