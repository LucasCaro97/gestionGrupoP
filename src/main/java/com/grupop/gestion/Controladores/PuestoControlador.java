package com.grupop.gestion.Controladores;

import com.grupop.gestion.Entidades.Puesto;
import com.grupop.gestion.Entidades.TipoIva;
import com.grupop.gestion.Servicios.PuestoServicio;
import com.grupop.gestion.Servicios.TipoIvaServicio;
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
@RequestMapping("/puesto")
public class PuestoControlador {

    private final PuestoServicio puestoServicio;

    @GetMapping
    public ModelAndView getAll(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("tabla-puesto");
        Map<String,?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);

        if(inputFlashMap != null) mav.addObject("exito", inputFlashMap.get("exito"));
        mav.addObject("listaPuesto", puestoServicio.obtenerTodos());
        return mav;
    }

    @GetMapping("/form")
    public ModelAndView getForm(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("form-puesto");
        Map<String,?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);

        if(inputFlashMap != null){
            mav.addObject("puesto", inputFlashMap.get("puesto"));
            mav.addObject("exception", inputFlashMap.get("exception"));
        }else {
            mav.addObject("puesto", new Puesto());
            mav.addObject("listaPuesto", puestoServicio.obtenerTodos());
        }
        mav.addObject("action", "create");
        return mav;
    }

    @GetMapping("/form/{id}")
    public ModelAndView getFormUpdate(@PathVariable Long id){
        ModelAndView mav = new ModelAndView("form-puesto");
        mav.addObject("puesto", puestoServicio.buscarPorId(id));
        mav.addObject("action", "update");
        mav.addObject("listaPuesto", puestoServicio.obtenerTodos());
        return mav;
    }

    @PostMapping("/create")
    public RedirectView create(Puesto dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/puesto");
        try{
            puestoServicio.crear(dto);
            attributes.addFlashAttribute("exito", "La operacion se ha realizado con exito");
        } catch (IllegalArgumentException e){
            attributes.addFlashAttribute("puesto", dto);
            attributes.addFlashAttribute("exception", e.getMessage());
            redirect.setUrl("/puesto/form");
        }
        return redirect;
    }

    @PostMapping("/update")
    public RedirectView update(Puesto dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/puesto");

        try{
            puestoServicio.actualizar(dto);
            attributes.addFlashAttribute("exito", "La operacion se ha realizado con exito");
        }catch(IllegalArgumentException e){
            attributes.addFlashAttribute("puesto", dto);
            attributes.addFlashAttribute("exception", e.getMessage());
            redirect.setUrl("/puesto/form");
        }
        return redirect;
    }

    @GetMapping("/delete/{id}")
    public RedirectView deleteById(@PathVariable Long id, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/puesto");
        puestoServicio.eliminarPorId(id);
        attributes.addFlashAttribute("exito", "La operacion se ha realizado con exito");
        return redirect;
    }


}
