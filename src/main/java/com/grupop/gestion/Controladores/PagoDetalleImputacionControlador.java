package com.grupop.gestion.Controladores;

import com.grupop.gestion.Servicios.PagoDetalleImputacionServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

@Controller
@RequiredArgsConstructor
@RequestMapping("pagoDetalleImp")
public class PagoDetalleImputacionControlador {

    private final PagoDetalleImputacionServicio pagoDetalleImputacionServicio;


    @PostMapping("altaDetalle/{idPago}/{idCuenta}/{importe}")
    public ResponseEntity<String> altaDetalle(@PathVariable Long idPago, @PathVariable Long idCuenta,
                                              @PathVariable BigDecimal importe, RedirectAttributes attributes){
        try{
            pagoDetalleImputacionServicio.crear(idPago,idCuenta, importe);
            attributes.addFlashAttribute("exito", "Se guardaron los cambios de detalle correctamente");
        }catch(Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            System.out.println(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Registro creado correctamente");
    }

    @PostMapping("/bajaDetalle/{idPago}/{idCuenta}")
    public ResponseEntity<String> bajaDetalle(@PathVariable Long idPago, @PathVariable Long idCuenta, RedirectAttributes attributes){
        try{
            pagoDetalleImputacionServicio.eliminarPorPagoAndCuenta(idPago, idCuenta);
            attributes.addFlashAttribute("exito", "Se eliminaron los registros correctamente");
        } catch (Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            System.out.println(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Registro eliminado correctamente");
    }


}
