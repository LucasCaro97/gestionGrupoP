package com.grupop.gestion.Controladores;

import com.grupop.gestion.Entidades.CobroDetalleAdelanto;
import com.grupop.gestion.Servicios.CobroDetalleAdelantoServicio;
import lombok.RequiredArgsConstructor;
import org.hibernate.tool.schema.internal.exec.ScriptTargetOutputToFile;
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
@RequestMapping("/adelanto")
public class CobroDetalleAdelantoControlador {

    private final CobroDetalleAdelantoServicio cobroDetalleAdelantoServicio;


    @PostMapping("/generarAdelanto/{idCliente}/{idOperacion}/{importeAdelanto}/{detalle}")
    public ResponseEntity<String> altaDetalle(@PathVariable Long idCliente, @PathVariable Long idOperacion,
                                              @PathVariable BigDecimal importeAdelanto, @PathVariable String detalle){
        try{
            cobroDetalleAdelantoServicio.crear(idCliente, idOperacion, importeAdelanto, detalle);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Registro creado exitosamente");
    }


    @GetMapping("bajaAdelanto/{idAdelanto}")
    public RedirectView bajaDetalle(@PathVariable Long idAdelanto){
        CobroDetalleAdelanto c = cobroDetalleAdelantoServicio.obtenerPorId(idAdelanto);

        RedirectView r = new RedirectView("/cobros/form/" + c.getCobroId().getId());
        try{
            cobroDetalleAdelantoServicio.eliminarPorId(c.getId());
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return r;
    }


}
