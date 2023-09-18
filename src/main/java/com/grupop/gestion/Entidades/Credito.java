package com.grupop.gestion.Entidades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Credito {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "fk_cliente")
    @ManyToOne
    private Cliente cliente;
    private LocalDate fecha;
    @JoinColumn(name = "fk_sector")
    @ManyToOne
    private Sector sector;
    @JoinColumn(name = "fk_tipo_comprobante")
    @ManyToOne
    private TipoComprobante tipoComprobante;
    @Column(name = "fk_talonario")
    private Integer talonario;
    private String nroComprobante;
    @JoinColumn(name = "fk_venta")
    @OneToOne
    private Venta venta;
    @JoinColumn(name = "fk_plan_pago")
    @ManyToOne
    private PlanPago planPago;
    private Integer cantCuotas;
    private BigDecimal porcentajeInteres;
    private Integer vencimiento;
    private BigDecimal interesesTotales;
    private BigDecimal gastosAdministrativos;
    private BigDecimal capital;
    private BigDecimal totalCredito;
    private String observaciones;
    private boolean bloqueado;
    @JoinColumn(name = "fk_forma_pago")
    @OneToOne
    private FormaDePagoDetalleSubDetalle detallePago;
    @JoinColumn(name = "estado")
    @ManyToOne
    private EstadoCredito estadoCredito;

}
