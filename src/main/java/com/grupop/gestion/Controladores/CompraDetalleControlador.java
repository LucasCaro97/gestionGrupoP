package com.grupop.gestion.Controladores;

import com.grupop.gestion.Servicios.CompraDetalleServicio;
import com.grupop.gestion.Servicios.CompraServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;


@RequiredArgsConstructor
@RestController
@RequestMapping("comprasDetalle")
public class CompraDetalleControlador {


    private final CompraDetalleServicio compraDetalleServicio;
    private final CompraServicio compraServicio;

    @PostMapping("/altaDetalle/{idCompra}/{idProd}/{cantidad}/{precioU}/{precioF}")
    public ResponseEntity<String> altaDetalle(@PathVariable Long idCompra, @PathVariable Long idProd, @PathVariable BigDecimal cantidad,
                                              @PathVariable BigDecimal precioU, @PathVariable BigDecimal precioF, RedirectAttributes attributes){

    try{
        compraDetalleServicio.crear(idCompra, idProd, cantidad, precioU, precioF);
        attributes.addFlashAttribute("exito", "Se guardaron los cambios de detalle correctamente");
    } catch (Exception e){
        attributes.addFlashAttribute("exception", e.getMessage());
        System.out.println(e.getMessage());
    }
    return ResponseEntity.status(HttpStatus.CREATED).body("Registro creado exitosamente");

    }

    @PostMapping("/bajaDetalle/{idCompra}/{idProd}")
    public ResponseEntity<String> bajaDetalle(@PathVariable Long idCompra, @PathVariable Long idProd, RedirectAttributes attributes){
        try{
            compraDetalleServicio.eliminar(idCompra, idProd);
            System.out.println("Eliminar COMPRA - OK");
        } catch (Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            System.out.println(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Registro eliminado correctamente");
    }

    @PostMapping("/actualizarTotalCompra/{idCompra}/{total}")
    public ResponseEntity<String> actualizarTotal(@PathVariable Long idCompra, @PathVariable String total, RedirectAttributes attributes){
        try{
            Double totalDouble = Double.valueOf(total);
            compraServicio.actualizarTotal(idCompra,totalDouble);
            System.out.println("Se actualizo el total correctamente");
        } catch (Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            System.out.println(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Se actualizo el total correctamente");
    }

}
