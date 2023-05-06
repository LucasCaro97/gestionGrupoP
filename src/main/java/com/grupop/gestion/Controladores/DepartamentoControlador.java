package com.grupop.gestion.Controladores;

import com.grupop.gestion.Entidades.Departamento;
import com.grupop.gestion.Entidades.Puesto;
import com.grupop.gestion.Servicios.DepartamentoServicio;
import com.grupop.gestion.Servicios.PuestoServicio;
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
@RequestMapping("/departamento")
public class DepartamentoControlador {

    private final DepartamentoServicio departamentoServicio;

    @GetMapping
    public ModelAndView getAll(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("tabla-departamento");
        Map<String,?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);

        if(inputFlashMap != null) mav.addObject("exito", inputFlashMap.get("exito"));
        mav.addObject("listaDepartamento", departamentoServicio.obtenerTodos());
        return mav;
    }

    @GetMapping("/form")
    public ModelAndView getForm(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("form-departamento");
        Map<String,?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);

        if(inputFlashMap != null){
            mav.addObject("departamento", inputFlashMap.get("departamento"));
            mav.addObject("exception", inputFlashMap.get("exception"));
        }else {
            mav.addObject("departamento", new Departamento());
            mav.addObject("listaDepartamento", departamentoServicio.obtenerTodos());
        }
        mav.addObject("action", "create");
        return mav;
    }

    @GetMapping("/form/{id}")
    public ModelAndView getFormUpdate(@PathVariable Long id){
        ModelAndView mav = new ModelAndView("form-departamento");
        mav.addObject("departamento", departamentoServicio.buscarPorId(id));
        mav.addObject("action", "update");
        mav.addObject("listaDepartamento", departamentoServicio.obtenerTodos());
        return mav;
    }

    @PostMapping("/create")
    public RedirectView create(Departamento dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/departamento");
        try{
            departamentoServicio.crear(dto);
            attributes.addFlashAttribute("exito", "La operacion se ha realizado con exito");
        } catch (IllegalArgumentException e){
            attributes.addFlashAttribute("departamento", dto);
            attributes.addFlashAttribute("exception", e.getMessage());
            redirect.setUrl("/departamento/form");
        }
        return redirect;
    }

    @PostMapping("/update")
    public RedirectView update(Departamento dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/departamento");

        try{
            departamentoServicio.actualizar(dto);
            attributes.addFlashAttribute("exito", "La operacion se ha realizado con exito");
        }catch(IllegalArgumentException e){
            attributes.addFlashAttribute("departamento", dto);
            attributes.addFlashAttribute("exception", e.getMessage());
            redirect.setUrl("/departamento/form");
        }
        return redirect;
    }

    @GetMapping("/delete/{id}")
    public RedirectView deleteById(@PathVariable Long id, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/departamento");
        departamentoServicio.eliminarPorId(id);
        attributes.addFlashAttribute("exito", "La operacion se ha realizado con exito");
        return redirect;
    }


}

