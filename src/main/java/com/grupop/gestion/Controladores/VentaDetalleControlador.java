package com.grupop.gestion.Controladores;

import com.grupop.gestion.Servicios.VentaDetalleServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("ventaDetalle")
public class VentaDetalleControlador {

    private final VentaDetalleServicio ventaDetalleServicio;


    @PostMapping("/altaDetalle/{idVenta}/{idProd}/{cantidad}/{precioU}")
    public ResponseEntity<String> altaDetalle(@PathVariable Long idVenta, @PathVariable Long idProd, @PathVariable Double cantidad,
                                            @PathVariable BigDecimal precioU, RedirectAttributes attributes){
        try{
            ventaDetalleServicio.crear(idVenta,idProd,cantidad,precioU);
            System.out.println("Se creo el detalle con exito");
        }catch(Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            System.out.println(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Registro creado exitosamente");
    }


}
