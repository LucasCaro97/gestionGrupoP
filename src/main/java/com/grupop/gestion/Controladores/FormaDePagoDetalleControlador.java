package com.grupop.gestion.Controladores;

import com.grupop.gestion.Entidades.FormaDePagoDetalle;
import com.grupop.gestion.Servicios.FormaDePagoDetalleServicio;
import com.grupop.gestion.Servicios.FormaDePagoDetalleSubDetalleServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
@RequestMapping("/detalleDePago")
public class FormaDePagoDetalleControlador {

    private final FormaDePagoDetalleServicio formaDePagoDetalleServicio;
    private final FormaDePagoDetalleSubDetalleServicio formaDePagoDetalleSubDetalleServicio;

    @GetMapping("getForm/{idOperacion}/{idTipoOperacion}")
    public ModelAndView getFormUpd(@PathVariable Long idOperacion, @PathVariable Long idTipoOperacion){
        ModelAndView mav = new ModelAndView("form-detalleDePago");
        mav.addObject("detallePago", formaDePagoDetalleServicio.obtenerPorId(idOperacion, idTipoOperacion));
        mav.addObject("listaDetallePagoSubdetalle", formaDePagoDetalleSubDetalleServicio.obtenerPorMaestro(idOperacion,idTipoOperacion));
        return mav;
    }
}
