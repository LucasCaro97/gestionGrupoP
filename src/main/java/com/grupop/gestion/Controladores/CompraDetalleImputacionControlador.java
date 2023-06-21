package com.grupop.gestion.Controladores;

import com.grupop.gestion.Servicios.CompraDetalleImputacionServicio;
import com.grupop.gestion.Servicios.CompraServicio;
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
@RequestMapping("compraDetalleImputacion")
public class CompraDetalleImputacionControlador {

    private final CompraDetalleImputacionServicio compraDetalleImputacionServicio;
    private final CompraServicio compraServicio;

    @PostMapping("altaDetalle/{idCompra}/{idCuenta}/{importe}")
    public ResponseEntity<String> altaDetalle(@PathVariable Long idCompra, @PathVariable Long idCuenta,
                                              @PathVariable Double importe, RedirectAttributes attributes){
        try{
            compraDetalleImputacionServicio.crear(idCompra,idCuenta, importe);
            attributes.addFlashAttribute("exito", "Se guardaron los cambios de detalle correctamente");
        }catch(Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            System.out.println(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Registro creado correctamente");
    }

    @PostMapping("/bajaDetalle/{idCompra}/{idCuenta}")
    public ResponseEntity<String> bajaDetalle(@PathVariable Long idCompra, @PathVariable Long idCuenta, RedirectAttributes attributes){
        try{
            compraDetalleImputacionServicio.eliminar(idCompra, idCuenta);
            attributes.addFlashAttribute("exito", "Se guardaron los cambios correctamente");
        } catch (Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            System.out.println(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Registro eliminado correctamente");
    }
}
