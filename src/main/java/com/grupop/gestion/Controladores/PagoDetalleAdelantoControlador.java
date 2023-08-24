package com.grupop.gestion.Controladores;

import com.grupop.gestion.Entidades.CobroDetalleAdelanto;
import com.grupop.gestion.Entidades.PagoDetalleAdelanto;
import com.grupop.gestion.Servicios.CobroDetalleAdelantoServicio;
import com.grupop.gestion.Servicios.PagoDetalleAdelantoServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.math.BigDecimal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/adelantoProv")
public class PagoDetalleAdelantoControlador {

    private final PagoDetalleAdelantoServicio pagoDetalleAdelantoServicio;


    @PostMapping("/generarAdelanto/{idProv}/{idOperacion}/{importeAdelanto}/{detalle}")
    public ResponseEntity<String> altaDetalle(@PathVariable Long idProv, @PathVariable Long idOperacion,
                                              @PathVariable BigDecimal importeAdelanto, @PathVariable String detalle){
        try{
            pagoDetalleAdelantoServicio.crear(idProv, idOperacion, importeAdelanto, detalle);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Registro creado exitosamente");
    }


    @GetMapping("bajaAdelanto/{idAdelanto}")
    public RedirectView bajaDetalle(@PathVariable Long idAdelanto){
        PagoDetalleAdelanto p = pagoDetalleAdelantoServicio.obtenerPorId(idAdelanto);

        RedirectView r = new RedirectView("/pago/form/" + p.getPagoId().getId());
        try{
            pagoDetalleAdelantoServicio.eliminarPorId(p.getId());
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return r;
    }


}
