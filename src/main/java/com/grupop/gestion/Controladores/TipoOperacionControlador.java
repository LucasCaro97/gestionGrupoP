package com.grupop.gestion.Controladores;

import com.grupop.gestion.Entidades.TipoOperacion;
import com.grupop.gestion.Servicios.TipoOperacionServicio;
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
@RequestMapping("/tipoOperacion")
public class TipoOperacionControlador {

    private final TipoOperacionServicio tipoOperacionServicio;

    @GetMapping
    public ModelAndView getAll(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("tabla-tipoOperacion");
        Map<String,?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if(inputFlashMap != null){ mav.addObject("exito", inputFlashMap.get("exito")); }
        mav.addObject("listaOperaciones", tipoOperacionServicio.obtenerTodos());
        return  mav;
    }

    @GetMapping("/form")
    public ModelAndView getForm(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("form-tipoOperacion");
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if(inputFlashMap != null){
            mav.addObject("tipoOperacion", inputFlashMap.get("tipoOperacion"));
            mav.addObject("exception", inputFlashMap.get("exception"));
        }else{
            mav.addObject("tipoOperacion", new TipoOperacion());
            mav.addObject("action", "create");
        }
        return mav;
    }

    @GetMapping("/form/{id}")
    public ModelAndView getFormUpd(@PathVariable Long id){
        ModelAndView mav = new ModelAndView("form-tipoOperacion");
        mav.addObject("tipoOperacion", tipoOperacionServicio.obtenerPorId(id));
        mav.addObject("listaOperaciones", tipoOperacionServicio.obtenerTodos());
        mav.addObject("action", "update");
        return mav;
    }

    @PostMapping("/create")
    public RedirectView create(TipoOperacion dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/tipoOperacion");
        try{
            tipoOperacionServicio.crear(dto);
            attributes.addFlashAttribute("exito", "Se ha creado correctamente el tipo de operacion");
        }catch (Exception e){
            attributes.addFlashAttribute("tipoOperacion", dto);
            attributes.addFlashAttribute("exception", e.getMessage());
            redirect.setUrl("/tipoOperacion/form");
        }
        return redirect;
    }

    @PostMapping("/update")
    public RedirectView update(TipoOperacion dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/tipoOperacion");
        try {
            tipoOperacionServicio.actualizar(dto);
            attributes.addFlashAttribute("exito", "Se ha actualizado correctamente el tipo de operacion");
        } catch (Exception e){
            attributes.addFlashAttribute("tipoOperacion", dto);
            attributes.addFlashAttribute("exception", e.getMessage());
            redirect.setUrl("/tipoOperacion/form");
        }
        return redirect;
    }


    public RedirectView delete(@PathVariable Long id, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/tipoOperacion");
        tipoOperacionServicio.eliminarPorId(id);
        attributes.addFlashAttribute("exito", "Se ha eliminado el tipo de operacion correctamente");
        return redirect;
    }

}
