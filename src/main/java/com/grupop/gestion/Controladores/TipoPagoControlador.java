package com.grupop.gestion.Controladores;

import com.grupop.gestion.Entidades.TipoPago;
import com.grupop.gestion.Servicios.TipoPagoServicio;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("tipoDePago")
public class TipoPagoControlador {

    private final TipoPagoServicio tipoPagoServicio;

    @GetMapping
    public ModelAndView getAll(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("tabla-tipoDePago");
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if (inputFlashMap != null) {    mav.addObject("exito", inputFlashMap.get("exito")); }
        mav.addObject("listaPago", tipoPagoServicio.obtenerTodos());
        return mav;

    }

    @GetMapping("/form")
    public ModelAndView getForm(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("form-tipoDePago");
        Map<String,?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if(inputFlashMap!=null){
            mav.addObject("tipoPago", inputFlashMap.get("tipoPago"));
            mav.addObject("exception", inputFlashMap.get("exception"));
        }else{
            mav.addObject("tipoPago", new TipoPago());
            mav.addObject("listaPago", tipoPagoServicio.obtenerTodos());
        }
        mav.addObject("action", "create");
        return mav;
    }

    @GetMapping("/form/{id}")
    public ModelAndView getFormUpd(@PathVariable Long id){
        ModelAndView mav = new ModelAndView("form-tipoDePago");
        mav.addObject("action", "update");
        mav.addObject("tipoPago", tipoPagoServicio.obtenerPorId(id));
        mav.addObject("listaPago", tipoPagoServicio.obtenerTodos());
        return mav;
    }

    @PostMapping("/create")
    public RedirectView create(TipoPago dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/tipoDePago");
        try{
            tipoPagoServicio.crear(dto);
            attributes.addFlashAttribute("exito", "Se ha creado correctamente la forma de pago");
        }catch (Exception e){
            attributes.addFlashAttribute("tipoPago", dto);
            attributes.addFlashAttribute("exception", e.getMessage());
            redirect.setUrl("/tipoDePago/form");
        }
        return redirect;
    }


    @PostMapping("/update")
    public RedirectView update(TipoPago dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/tipoDePago");
        try {
            tipoPagoServicio.actualizar(dto);
            attributes.addFlashAttribute("exito", "Se ha actualizado correctamente la forma de pago");
        }catch(Exception e){
            attributes.addFlashAttribute("tipoPago", dto);
            attributes.addFlashAttribute("exception", e.getMessage());
            redirect.setUrl("/tipoDePago/form");
        }
        return redirect;
    }


    @PostMapping("/delete/{id}")
    public RedirectView delete(@PathVariable Long id, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/tipoDePago");
        tipoPagoServicio.elimianrPorId(id);
        attributes.addFlashAttribute("exito", "Se ha eliminado correctamente la forma de pago");
        return redirect;
    }
}
