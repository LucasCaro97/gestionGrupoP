package com.grupop.gestion.Controladores;

import com.grupop.gestion.Entidades.FormaDePago;
import com.grupop.gestion.Entidades.TipoOperacion;
import com.grupop.gestion.Entidades.TipoPago;
import com.grupop.gestion.Servicios.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/formaDePago")
public class FormaDePagoControlador {

    private final FormaDePagoServicio formaDePagoServicio;
    private final TipoOperacionServicio tipoOperacionServicio;
    private final TipoPagoServicio tipoPagoServicio;
    private final CuentasDeBancoTarjetaServicio cuentasDeBancoTarjetaServicio;
    private final MonedaServicio monedaServicio;
    private final CuentasContablesServicio cuentasContablesServicio;

    @GetMapping
    public ModelAndView getAll(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("tabla-formaDePago");
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if (inputFlashMap != null) {
            mav.addObject("exito", inputFlashMap.get("exito"));
        }
        mav.addObject("listafdp", formaDePagoServicio.obtenerTodos());
        return mav;
    }

    @GetMapping("/form")
    public ModelAndView getForm(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("form-formaDePago");
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if (inputFlashMap != null) {
            mav.addObject("formaDePago", inputFlashMap.get("formaDePago"));
            mav.addObject("exception", inputFlashMap.get("exception"));
        } else {
            mav.addObject("formaDePago", new FormaDePago());
        }
        mav.addObject("listaTipo", tipoPagoServicio.obtenerTodos());
        mav.addObject("listaTipoOp", tipoOperacionServicio.obtenerTodos());
        mav.addObject("listaBot", cuentasDeBancoTarjetaServicio.obtenerTodos());
        mav.addObject("listaMon", monedaServicio.obtenerTodos());
        mav.addObject("listaCuentas", cuentasContablesServicio.obtenerTodos());
        mav.addObject("action", "create");
        return mav;
    }

    @GetMapping("/form/{id}")
    public ModelAndView getFormUpd(@PathVariable Long id){
        ModelAndView mav = new ModelAndView("form-formaDePago");
        mav.addObject("action", "update");
        mav.addObject("formaDePago", formaDePagoServicio.obtenerPorId(id));
        mav.addObject("listaTipo", tipoPagoServicio.obtenerTodos());
        mav.addObject("listaTipoOp", tipoOperacionServicio.obtenerTodos());
        mav.addObject("listaBot", cuentasDeBancoTarjetaServicio.obtenerTodos());
        mav.addObject("listaMon", monedaServicio.obtenerTodos());
        mav.addObject("listaCuentas", cuentasContablesServicio.obtenerTodos());
        return mav;
    }

    @PostMapping("/create")
    public RedirectView create(FormaDePago dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/formaDePago");
        try{
            formaDePagoServicio.crear(dto);
            attributes.addFlashAttribute("exito", "Se ha creado correctamente la forma de pago");
        }catch(Exception e){
            attributes.addFlashAttribute("formaDePago", dto);
            attributes.addFlashAttribute("exception", e.getMessage());
            redirect.setUrl("/formaDePago/form");
        }
        return redirect;
    }

    @PostMapping("/update")
    public RedirectView update(FormaDePago dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/formaDePago");
        try {
            formaDePagoServicio.actualizar(dto);
            attributes.addFlashAttribute("exito", "Se ha actualizado correctamente la forma de pago");
        }catch (Exception e){
            attributes.addFlashAttribute("formaDepago", dto);
            attributes.addFlashAttribute("exception", e.getMessage());
            redirect.setUrl("/formaDePago/form");
        }
        return redirect;
    }

    @GetMapping("/delete")
    public RedirectView delete(@PathVariable Long id, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/formaDePago");
        formaDePagoServicio.eliminarPorId(id);
        attributes.addFlashAttribute("exito", "Se ha eliminado correctamente la forma de pago");
        return redirect;
    }

    @GetMapping("obtenerPorOperacion/{id}")
     public ResponseEntity<List<FormaDePago>> getFormasPagoPorOperacion(@PathVariable Long id){
        return ResponseEntity.ok(formaDePagoServicio.obtenerTodosPorOperacion(id));
    }



}
