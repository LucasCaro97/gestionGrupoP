package com.grupop.gestion.Controladores;

import com.grupop.gestion.DTO.CobroDetalleCtaCteDTO;
import com.grupop.gestion.Entidades.CobroDetalleCtaCte;
import com.grupop.gestion.Servicios.CobroDetalleCtaCteServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("cobroDetalleCtaCte")
public class CobroDetalleCtaCteControlador {

    private final CobroDetalleCtaCteServicio cobroDetalleCtaCteServicio;

    @PostMapping("/alta/{idCobro}/{idVenta}/{importe}")
    public ResponseEntity<String> altaDetalle(@PathVariable Long idCobro, @PathVariable Long idVenta, @PathVariable BigDecimal importe){
        try{
            cobroDetalleCtaCteServicio.crear(idCobro, idVenta, importe);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Registro creado exitosamente");

    }

    @PostMapping("/alta")
    public ResponseEntity<String> altaDetalle(@RequestBody List<CobroDetalleCtaCteDTO> listaItems){
        try{
            cobroDetalleCtaCteServicio.crear(listaItems);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Registro creado exitosamente");

    }


    @PostMapping("/bajaDetalle/{idDetalle}")
    public ResponseEntity<String> bajaDetalle (@PathVariable Long idDetalle){
        try{
            cobroDetalleCtaCteServicio.eliminar(idDetalle);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Registro eliminado correctamente");
    }




}
