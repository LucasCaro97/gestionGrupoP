/*
package com.grupop.gestion.Controladores;

import com.grupop.gestion.Entidades.ClasificacionCta;
import com.grupop.gestion.Servicios.ClasifiacionCtaServicio;
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
@RequestMapping("/clasificacionCta")
public class ClasificacionCtaControlador {

    private final ClasifiacionCtaServicio clasifiacionCtaServicio;

    @GetMapping
    public ModelAndView getAll(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("tabla-clasificacionCta");
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);

        if (inputFlashMap != null) {
            mav.addObject("exito", inputFlashMap.get("exito"));
        }
        mav.addObject("listaClasif", clasifiacionCtaServicio.obtenerTodos());
        return mav;
    }

    @GetMapping("/form")
    public ModelAndView getForm(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("form-clasificacionCta");
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);

        if (inputFlashMap != null) {
            mav.addObject("clasificacion", inputFlashMap.get("clasificacion"));
            mav.addObject("exception", inputFlashMap.get("exception"));
        } else {
            mav.addObject("clasificacion", new ClasificacionCta());
        }
        mav.addObject("action", "create");
        return mav;
    }

    @GetMapping("/form/{id}")
    public ModelAndView getFormUpd(@PathVariable Long id){
        ModelAndView mav = new ModelAndView("form-clasificacionCta");
        mav.addObject("action", "update");
        mav.addObject("clasificacion", clasifiacionCtaServicio.obtenerPorId(id));
        return mav;

    }

    @PostMapping("/create")
    public RedirectView create(ClasificacionCta dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/clasificacionCta");
        try{
            clasifiacionCtaServicio.crear(dto);
            attributes.addFlashAttribute("exito", "Se ha creado correctamente la clasificion de la cuenta");
        }catch (Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            attributes.addFlashAttribute("clasificacion", dto);
            redirect.setUrl("/clasificacionCta/form");
        }
        return redirect;
    }

    @PostMapping("/update")
    public RedirectView update(ClasificacionCta dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/clasificacionCta");
        try{
            clasifiacionCtaServicio.actualizar(dto);
            attributes.addFlashAttribute("exito", "Se ha actualizado correctamente la clasificacion de la cuenta");
        }catch (Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            attributes.addFlashAttribute("clasificacion", dto);
            redirect.setUrl("/clasificacionCta/form");
        }
        return redirect;
    }

    @GetMapping("/delete")
    public RedirectView delete(@PathVariable Long id, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/clasificacionCta");
        clasifiacionCtaServicio.eliminarPorId(id);
        attributes.addFlashAttribute("exito", "Se ha eliminado correctamente la clasificicacion de la cuenta");
        return redirect;
    }





}


 */
