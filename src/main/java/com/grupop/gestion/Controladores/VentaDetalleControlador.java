package com.grupop.gestion.Controladores;

import com.grupop.gestion.DTO.VentaDetalleDTO;
import com.grupop.gestion.Servicios.VentaDetalleServicio;
import com.grupop.gestion.Servicios.VentaServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("ventaDetalle")
public class VentaDetalleControlador {

    private final VentaDetalleServicio ventaDetalleServicio;
    private final VentaServicio ventaServicio;


    @PostMapping("/altaDetalle/{idVenta}/{idProd}/{cantidad}/{precioU}")
    public ResponseEntity<String> altaDetalle(@PathVariable Long idVenta, @PathVariable Long idProd, @PathVariable Double cantidad,
                                            @PathVariable Double precioU, RedirectAttributes attributes){
        try{
            ventaDetalleServicio.crear(idVenta,idProd,cantidad,precioU);
        }catch(Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            System.out.println(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Registro creado exitosamente");
    }

    @PostMapping("/altaDetalle")
    public ResponseEntity<String> altaDetalle(@RequestBody List<VentaDetalleDTO> listaItems, RedirectAttributes attributes){
        try{
            ventaDetalleServicio.crear(listaItems);
        }catch(Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            System.out.println(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Registro creado exitosamente");
    }

    @PostMapping("/bajaDetalle/{idVenta}/{idProd}")
    public ResponseEntity<String> bajaDetalle(@PathVariable Long idVenta, @PathVariable Long idProd, RedirectAttributes attributes){
        try{
            ventaDetalleServicio.eliminar(idVenta, idProd);
            System.out.println("Eliminar VENTA - OK");
        }catch(Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            System.out.println(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Registro eliminado correctamente");
    }


}
