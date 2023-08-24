package com.grupop.gestion.Controladores;

import com.grupop.gestion.Entidades.EntidadBase;
import com.grupop.gestion.Entidades.FormaDePago;
import com.grupop.gestion.Entidades.FormaDePagoDetalle;
import com.grupop.gestion.Servicios.*;
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
    private final VentaServicio ventaServicio;
    private final CompraServicio compraServicio;
    private final CobroServicio cobroServicio;
    private final PagoServicio pagoServicio;


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

    @GetMapping("/validarEstado/{idOperacion}/{idTipoOperacion}")
    public ResponseEntity<Boolean> obtenerEstado(@PathVariable Long idOperacion, @PathVariable Long idTipoOperacion){
        return ResponseEntity.ok(formaDePagoDetalleServicio.validarEstado(idOperacion, idTipoOperacion));
    }

    @GetMapping("/obtenerSaldoEntidad/{idOperacion}/{idTipoOperacion}")
    public ResponseEntity<BigDecimal> obtenerCliente(@PathVariable Long idOperacion, @PathVariable Long idTipoOperacion){

        ResponseEntity response = null;

        if(idTipoOperacion == 1){
            response = ResponseEntity.ok(ventaServicio.obtenerSaldoCliente(idOperacion));
        } else if (idTipoOperacion == 2) {
            response = ResponseEntity.ok(compraServicio.obtenerSaldoProveedor(idOperacion));
        } else if (idTipoOperacion == 3) {
            response = ResponseEntity.ok(cobroServicio.obtenerSaldoCliente(idOperacion));
        } else if (idTipoOperacion == 4) {
            response = ResponseEntity.ok(pagoServicio.obtenerSaldoProveedor(idOperacion));
        }

        return response;
    }

}
