package com.grupop.gestion.Controladores;


import com.grupop.gestion.Entidades.TipoTotalizadora;
import com.grupop.gestion.Servicios.TipoTotalizadoraServicio;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
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
@RequestMapping("/tipoTot")
public class TipoTotalizadoraControlador {

    private final TipoTotalizadoraServicio tipoTotalizadoraServicio;

    @GetMapping
    public ModelAndView getAll(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("tabla-tipoTot");
        Map<String,?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if(inputFlashMap!=null) { mav.addObject("exito", inputFlashMap.get("exito"));  }
        mav.addObject("listaTipoTot", tipoTotalizadoraServicio.obtenerTodos());
        return mav;
    }
    @GetMapping("/form")
    public ModelAndView getForm(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("form-tipoTot");
        Map<String,?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if(inputFlashMap!=null){
            mav.addObject("tipoTot", inputFlashMap.get("tipoTot"));
            mav.addObject("exception", inputFlashMap.get("exception"));
        }else{
            mav.addObject("tipoTot", new TipoTotalizadora());
        }
        mav.addObject("action", "create");
        mav.addObject("listaTipoTot", tipoTotalizadoraServicio.obtenerTodos());
        return mav;
    }


    @GetMapping("/form/{id}")
    public ModelAndView getFormUpd(@PathVariable Long id){
        ModelAndView mav = new ModelAndView("form-tipoTot");
        mav.addObject("tipoTot", tipoTotalizadoraServicio.obtenerPorId(id));
        mav.addObject("action", "update");
        mav.addObject("listaTipoTot", tipoTotalizadoraServicio.obtenerTodos());
        return mav;
    }

    @PostMapping("/create")
    public RedirectView create(TipoTotalizadora dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/tipoTot");
        try{
            tipoTotalizadoraServicio.crear(dto);
            attributes.addFlashAttribute("exito", "Se ha creado correctamente el Tipo Totalizadora");
        }catch(Exception e){
            attributes.addFlashAttribute("tipoTot", dto);
            attributes.addFlashAttribute("exception", e.getMessage());
            redirect.setUrl("/tipoTot/form");
        }
        return redirect;
    }

    @PostMapping("/update")
    public RedirectView update(TipoTotalizadora dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/tipoTot");
        try{
            tipoTotalizadoraServicio.actualizar(dto);
            attributes.addFlashAttribute("exito", "Se ha actualizado correctamente el Tipo Totalizadora");
        }catch (Exception e){
            attributes.addFlashAttribute("tipoTot", dto);
            attributes.addFlashAttribute("exception", e.getMessage());
            redirect.setUrl("/tipoTot/form");
        }
        return redirect;
    }
    @GetMapping("/delete/{id}")
    public RedirectView delete(@PathVariable Long id, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/tipoTot");
        tipoTotalizadoraServicio.eliminarPorId(id);
        attributes.addFlashAttribute("exito", "Se ha eliminado correctamente el Tipo Total");
        return redirect;
    }



}
