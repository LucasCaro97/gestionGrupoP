package com.grupop.gestion.Controladores;

import com.grupop.gestion.Entidades.Turno;
import com.grupop.gestion.Servicios.TurnoServicio;
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
@RequestMapping("/turno")
public class TurnoControlador {

    private final TurnoServicio turnoServicio;

    @GetMapping
    public ModelAndView getAll(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("tabla-turno");
        Map<String,?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);

        if(inputFlashMap != null) mav.addObject("exito", inputFlashMap.get("exito"));
        mav.addObject("listaTurno", turnoServicio.obtenerTodos());
        return mav;
    }

    @GetMapping("/form")
    public ModelAndView getForm(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("form-turno");
        Map<String,?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);

        if(inputFlashMap != null){
            mav.addObject("turno", inputFlashMap.get("turno"));
            mav.addObject("exception", inputFlashMap.get("exception"));
        }else {
            mav.addObject("turno", new Turno());
            mav.addObject("listaTurno", turnoServicio.obtenerTodos());
        }
        mav.addObject("action", "create");
        return mav;
    }

    @GetMapping("/form/{id}")
    public ModelAndView getFormUpdate(@PathVariable Long id){
        ModelAndView mav = new ModelAndView("form-puesto");
        mav.addObject("turno", turnoServicio.buscarPorId(id));
        mav.addObject("action", "update");
        mav.addObject("listaTurno", turnoServicio.obtenerTodos());
        return mav;
    }

    @PostMapping("/create")
    public RedirectView create(Turno dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/turno");
        try{
            turnoServicio.crear(dto);
            attributes.addFlashAttribute("exito", "La operacion se ha realizado con exito");
        } catch (IllegalArgumentException e){
            attributes.addFlashAttribute("turno", dto);
            attributes.addFlashAttribute("exception", e.getMessage());
            redirect.setUrl("/turno/form");
        }
        return redirect;
    }

    @PostMapping("/update")
    public RedirectView update(Turno dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/turno");

        try{
            turnoServicio.actualizar(dto);
            attributes.addFlashAttribute("exito", "La operacion se ha realizado con exito");
        }catch(IllegalArgumentException e){
            attributes.addFlashAttribute("turno", dto);
            attributes.addFlashAttribute("exception", e.getMessage());
            redirect.setUrl("/turno/form");
        }
        return redirect;
    }

    @GetMapping("/delete/{id}")
    public RedirectView deleteById(@PathVariable Long id, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/turno");
        turnoServicio.eliminarPorId(id);
        attributes.addFlashAttribute("exito", "La operacion se ha realizado con exito");
        return redirect;
    }


}