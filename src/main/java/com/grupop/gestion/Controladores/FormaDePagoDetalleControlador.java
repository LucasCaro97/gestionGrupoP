package com.grupop.gestion.Controladores;

import com.grupop.gestion.Entidades.FormaDePago;
import com.grupop.gestion.Entidades.FormaDePagoDetalle;
import com.grupop.gestion.Servicios.FormaDePagoDetalleServicio;
import com.grupop.gestion.Servicios.FormaDePagoDetalleSubDetalleServicio;
import com.grupop.gestion.Servicios.FormaDePagoServicio;
import com.grupop.gestion.Servicios.TipoOperacionServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/detalleDePago")
public class FormaDePagoDetalleControlador {

    private final FormaDePagoDetalleServicio formaDePagoDetalleServicio;
    private final FormaDePagoDetalleSubDetalleServicio formaDePagoDetalleSubDetalleServicio;
    private final TipoOperacionServicio tipoOperacionServicio;
    private final FormaDePagoServicio formaDePagoServicio;

    @GetMapping("getForm/{idOperacion}/{idTipoOperacion}")
    public ModelAndView getFormUpd(@PathVariable Long idOperacion, @PathVariable Long idTipoOperacion){
        ModelAndView mav = new ModelAndView("form-detalleDePago");
        mav.addObject("detallePago", formaDePagoDetalleServicio.obtenerPorId(idOperacion, idTipoOperacion));
        mav.addObject("listaDetallePagoSubdetalle", formaDePagoDetalleSubDetalleServicio.obtenerPorMaestro(idOperacion,idTipoOperacion));
        mav.addObject("tipoOperacion", tipoOperacionServicio.obtenerPorId(idTipoOperacion));
        mav.addObject("listaFormaDePago", formaDePagoServicio.obtenerTodosPorOperacion(idTipoOperacion));
        return mav;
    }

    @GetMapping("/obtenerTotal/{idOperacion}/{idTipoOperacion}")
    public ResponseEntity<BigDecimal> obtenerTotal(@PathVariable Long idOperacion, @PathVariable Long idTipoOperacion){
        return ResponseEntity.ok(formaDePagoDetalleServicio.obtenerTotalDetallePago(idOperacion,idTipoOperacion));
    }

    @GetMapping("/obtenerCapitalCredito/{idOperacion}/{idTipoOperacion}")
    public ResponseEntity<BigDecimal> obtenerCapital(@PathVariable Long idOperacion, @PathVariable Long idTipoOperacion){
        return  ResponseEntity.ok(formaDePagoDetalleServicio.obtenerCapitalCredito(idOperacion, idTipoOperacion));
    }

}
