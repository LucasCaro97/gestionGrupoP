package com.grupop.gestion.Controladores;

import com.grupop.gestion.Entidades.Moneda;
import com.grupop.gestion.Servicios.MonedaServicio;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/moneda")
public class MonedaControlador {

    private final MonedaServicio monedaServicio;

    @GetMapping
    public ModelAndView getAll(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("tabla-moneda");
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);

        if(inputFlashMap != null) mav.addObject("exito", inputFlashMap.get("exito"));
        mav.addObject("listaMoneda", monedaServicio.obtenerTodos());
        return mav;
    }

    @GetMapping("/form")
    public ModelAndView getForm(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("form-moneda");
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if(inputFlashMap!=null){
            mav.addObject("moneda", inputFlashMap.get("moneda"));
            mav.addObject("exception", inputFlashMap.get("exception"));
        }else{
            mav.addObject("moneda", new Moneda());
            mav.addObject("listaMoneda", monedaServicio.obtenerTodos());
        }
        mav.addObject("action", "create");
        return mav;
    }

    @GetMapping("/form/{id}")
    public ModelAndView getFormUpdate(@PathVariable Long id){
        ModelAndView mav = new ModelAndView("form-moneda");
        mav.addObject("moneda", monedaServicio.obtenerPorId(id));
        mav.addObject("listaMoneda", monedaServicio.obtenerTodos());
        mav.addObject("action", "update");
        return mav;
    }

    @PostMapping("/create")
    public RedirectView create(Moneda dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/moneda");
        try{
            System.out.println("Ingrese al post de moneda");
            monedaServicio.crear(dto);
            attributes.addFlashAttribute("exito", "Se ha cargado la moneda correctamente");
        } catch (Exception e){
            attributes.addFlashAttribute("moneda", dto);
            attributes.addFlashAttribute("exception", e.getMessage());
            redirect.setUrl("/moneda/form");
        }
        return redirect;
    }

    @PostMapping("/update")
    public RedirectView update(Moneda dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/moneda");
        try{
            monedaServicio.actualizar(dto);
            attributes.addFlashAttribute("exito", "Se ha actualizado la moneda correctamente");
        } catch (Exception e){
            attributes.addFlashAttribute("moneda", dto);
            attributes.addFlashAttribute("excepion", e.getMessage());
            redirect.setUrl("/moneda/form");
        }
        return redirect;
    }



    @GetMapping("/delete/{id}")
    public RedirectView delete(@PathVariable Long id, RedirectAttributes attributes) {
        RedirectView redirect = new RedirectView("/moneda");
        monedaServicio.eliminarPorId(id);
        attributes.addFlashAttribute("exito", "Se ha eliminado correctamente el registro");
        return  redirect;
    }



}
