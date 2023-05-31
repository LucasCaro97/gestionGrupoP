package com.grupop.gestion.Controladores;

import com.grupop.gestion.Entidades.EstadoLote;
import com.grupop.gestion.Servicios.EstadoLoteServicio;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
@RequestMapping("/estadoLote")
public class EstadoLoteControlador {


    private final EstadoLoteServicio estadoLoteServicio;

    @GetMapping
    public ModelAndView getAll(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("tabla-estadoLote");
        Map<String,?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if(inputFlashMap!=null){ mav.addObject("exito", inputFlashMap.get("exito")); }
        mav.addObject("listaEstadoLote", estadoLoteServicio.obtenerTodos());
        return mav;
    }


    @GetMapping("/form")
    public ModelAndView getForm(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("form-estadoLote");
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if(inputFlashMap!=null){
            mav.addObject("estadoLote", inputFlashMap.get("estadoLote"));
            mav.addObject("exception", inputFlashMap.get("exception"));
        }else{
            mav.addObject("estadoLote", new EstadoLote());
        }
        mav.addObject("action", "create");
        mav.addObject("listaEstadoLote", estadoLoteServicio.obtenerTodos());
        return mav;
    }

    @GetMapping("/form/{id}")
    public ModelAndView getFormUpd(@PathVariable Long id){
        ModelAndView mav = new ModelAndView("form-estadoLote");
        mav.addObject("estadoLote", estadoLoteServicio.buscarPorId(id));
        mav.addObject("action", "update");
        mav.addObject("listaEstadoLote", estadoLoteServicio.obtenerTodos());
        return mav;
    }

    @PostMapping("/create")
    public RedirectView create(EstadoLote dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/estadoLote");
        try{
            estadoLoteServicio.crear(dto);
            attributes.addFlashAttribute("exito", "Se ha creado correctamente el estadoLote");
        }catch(Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            attributes.addFlashAttribute("estadoLote", dto);
            redirect.setUrl("estadoLote/form");
        }
        return redirect;
    }

    @PostMapping("/update")
    public RedirectView update(EstadoLote dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/estadoLote");
        try{
            estadoLoteServicio.crear(dto);
            attributes.addFlashAttribute("exito", "Se ha actualizado correctamente el estadoLote");
        }catch (Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            attributes.addFlashAttribute("estadoLote", dto);
            redirect.setUrl("/estadoLote/form");
        }
        return redirect;
    }

    @GetMapping("/delete")
    public RedirectView delete(@PathVariable Long id, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("estadoLote");
        estadoLoteServicio.eliminarPorId(id);
        attributes.addFlashAttribute("exito", "Se ha eliminado correctamente el estadoLote");
        return redirect;
    }



}

