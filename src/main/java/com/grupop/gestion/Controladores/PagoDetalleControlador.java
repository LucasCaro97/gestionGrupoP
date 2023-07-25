package com.grupop.gestion.Controladores;

import com.grupop.gestion.Servicios.PagoDetalleServicioCtaCte;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;

@Controller
@RequiredArgsConstructor
@RequestMapping("pagoDetalle")
public class PagoDetalleControlador {

    private final PagoDetalleServicioCtaCte pagoDetalleServicioCtaCte;

    @PostMapping("altaDetalle/{idPago}/{idCompra}/{importe}")
    public ResponseEntity<String> altaDetalle(@PathVariable Long idPago, @PathVariable Long idCompra, @PathVariable BigDecimal importe){
        try{
            System.out.println("Direcionando al servicio");
            pagoDetalleServicioCtaCte.crear(idPago, idCompra, importe);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Registro creado exitosamente");
    }

    @PostMapping("/bajaDetalle/{idDetalle}")
    public ResponseEntity<String> bajaDetalle (@PathVariable Long idDetalle){
        try{
            pagoDetalleServicioCtaCte.eliminarPorId(idDetalle);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Registro eliminado correctamente");
    }

}
