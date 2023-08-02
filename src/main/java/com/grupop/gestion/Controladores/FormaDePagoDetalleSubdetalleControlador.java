package com.grupop.gestion.Controladores;

import com.grupop.gestion.Entidades.FormaDePagoDetalle;
import com.grupop.gestion.Entidades.FormaDePagoDetalleSubDetalle;
import com.grupop.gestion.Servicios.FormaDePagoDetalleServicio;
import com.grupop.gestion.Servicios.FormaDePagoDetalleSubDetalleServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
@RequestMapping("formaDePagoDetalleSubdetalle")
public class FormaDePagoDetalleSubdetalleControlador {

    private final FormaDePagoDetalleSubDetalleServicio formaDePagoDetalleSubDetalleServicio;
    private final FormaDePagoDetalleServicio formaDePagoDetalleServicio;

    @PostMapping("/alta/{idOperacion}/{idTipoOperacion}/{importe}/{idFormaDePago}")
    public ResponseEntity<String> altaDetalle(@PathVariable Long idOperacion, @PathVariable Long idTipoOperacion, @PathVariable BigDecimal importe, @PathVariable Long idFormaDePago){
        try{
            formaDePagoDetalleServicio.crearSubdetalleManual(idOperacion, idTipoOperacion, idFormaDePago, importe);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Se ha creado el registro de pago correctamente");
    }

    @GetMapping("baja/{id}")
    public RedirectView bajaDetalle(@PathVariable Long id){
        RedirectView r = new RedirectView();
        try{
            FormaDePagoDetalleSubDetalle fsub = formaDePagoDetalleSubDetalleServicio.obtenerPorId(id);
            FormaDePagoDetalle f = formaDePagoDetalleServicio.obtenerPorId(fsub.getFormaDePagoDetalle().getIdOperacion(), fsub.getFormaDePagoDetalle().getTipoOperacion());
            formaDePagoDetalleSubDetalleServicio.eliminarPorId(id);
            r.setUrl("/detalleDePago/getForm/" + f.getIdOperacion() + "/" + f.getTipoOperacion());
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

    return r;
    }

}
