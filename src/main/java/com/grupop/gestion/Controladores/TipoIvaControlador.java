package com.grupop.gestion.Controladores;

import com.grupop.gestion.Entidades.TipoIva;
import com.grupop.gestion.Servicios.TipoIvaServicio;
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
@RequestMapping("/tipoIva")
public class TipoIvaControlador {

    private final TipoIvaServicio tipoIvaServicio;

    @GetMapping
    public ModelAndView getAll(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("tabla-tipoIva");
        Map<String,?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);

        if(inputFlashMap != null) mav.addObject("exito", inputFlashMap.get("exito"));
        mav.addObject("listaTipoIva", tipoIvaServicio.obtenerTodos());
        return mav;
    }

    @GetMapping("/form")
    public ModelAndView getForm(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("form-tipoIva");
        Map<String,?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);

        if(inputFlashMap != null){
            mav.addObject("tipoIva", inputFlashMap.get("tipoIva"));
            mav.addObject("exception", inputFlashMap.get("exception"));
        }else {
            mav.addObject("tipoIva", new TipoIva());
            mav.addObject("listaTipoIva", tipoIvaServicio.obtenerTodos());
        }
        mav.addObject("action", "create");
        return mav;
    }

    @GetMapping("/form/{id}")
    public ModelAndView getFormUpdate(@PathVariable Long id){
        ModelAndView mav = new ModelAndView("form-tipoIva");
        mav.addObject("tipoIva", tipoIvaServicio.buscarPorId(id));
        mav.addObject("action", "update");
        mav.addObject("listaTipoIva", tipoIvaServicio.obtenerTodos());
        return mav;
    }

    @PostMapping("/create")
    public RedirectView create(TipoIva dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/tipoIva");
            try{
                tipoIvaServicio.crear(dto);
                attributes.addFlashAttribute("exito", "La operacion se ha realizado con exito");
            } catch (IllegalArgumentException e){
                attributes.addFlashAttribute("tipoIva", dto);
                attributes.addFlashAttribute("exception", e.getMessage());
                redirect.setUrl("/tipoIva/form");
            }
        return redirect;
    }

    @PostMapping("/update")
    public RedirectView update(TipoIva dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/tipoIva");

        try{
            tipoIvaServicio.actualizar(dto);
            attributes.addFlashAttribute("exito", "La operacion se ha realizado con exito");
        }catch(IllegalArgumentException e){
            attributes.addFlashAttribute("tipoIva", dto);
            attributes.addFlashAttribute("exception", e.getMessage());
            redirect.setUrl("/tipoIva/form");
        }
        return redirect;
    }

    @DeleteMapping("/delete/{id}")
    public RedirectView deleteById(@PathVariable Long id){
        RedirectView redirect = new RedirectView("/tipoIva");
        tipoIvaServicio.eliminarPorId(id);
        return redirect;
    }



}
