package com.grupop.gestion.Controladores;

import com.grupop.gestion.Servicios.VentaDetalleImputacionServicio;
import com.grupop.gestion.Servicios.VentaServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@RequiredArgsConstructor
@RequestMapping("ventaDetalleImputacion")
public class VentaDetalleImputacionControlador {

    private final VentaDetalleImputacionServicio ventaDetalleImputacionServicio;
    private final VentaServicio ventaServicio;

    @PostMapping("/altaDetalle/{idVenta}/{idCuenta}/{importe}")
    public ResponseEntity<String> altaDetalle(@PathVariable Long idVenta, @PathVariable Long idCuenta,
                                              @PathVariable Double importe, RedirectAttributes attributes){
        try{
            ventaDetalleImputacionServicio.crear(idVenta, idCuenta, importe);
            attributes.addFlashAttribute("exito", "Se guardaron los cambios de detalle correctamente");
        }catch(Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            System.out.println(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Registro creado exitosamente");
    }

    @PostMapping("/bajaDetalle/{idVenta}/{idCuenta}")
    public ResponseEntity<String> bajaDetalle(@PathVariable Long idVenta, @PathVariable Long idCuenta,RedirectAttributes attributes){
        try{
            ventaDetalleImputacionServicio.eliminar(idVenta, idCuenta);
            attributes.addFlashAttribute("exito", "Se guardaron los cambios de detalle correctamente");
        }catch(Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            System.out.println(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Registro creado exitosamente");
    }

    @PostMapping("/bajaDetalle/{idVenta}/{total}")
    public ResponseEntity<String> actualizarTotal(@PathVariable Long idVenta, @PathVariable String total,RedirectAttributes attributes){
        try{
            Double totalDouble = Double.valueOf(total);
            ventaServicio.actualizarTotal(idVenta, totalDouble);
            attributes.addFlashAttribute("exito", "Se guardaron los cambios de detalle correctamente");
        }catch(Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            System.out.println(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Registro creado exitosamente");
    }

}
