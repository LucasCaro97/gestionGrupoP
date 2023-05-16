package com.grupop.gestion.Controladores;

import com.grupop.gestion.Entidades.Impuestos;
import com.grupop.gestion.Servicios.ImpuestosServicio;
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
@RequestMapping("/impuestosCta")
public class ImpuestosControlador {

    private final ImpuestosServicio impuestosServicio;

    @GetMapping
    public ModelAndView getAll(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("tabla-impuestos");
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);

        if(inputFlashMap != null){ mav.addObject("exito", inputFlashMap.get("exito"));       }
        mav.addObject("listaImpuestos", impuestosServicio.obtenerTodos());
        return mav;
    }

    @GetMapping("/form")
    public ModelAndView getForm(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("form-impuestos");
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);

        if(inputFlashMap != null){
            mav.addObject("impuesto", inputFlashMap.get("impuesto"));
            mav.addObject("exception", inputFlashMap.get("exception"));
        }else{
            mav.addObject("impuesto", new Impuestos());
        }
        mav.addObject("action", "create");
        return mav;
    }

    @GetMapping("/form/{id}")
    public ModelAndView getFormUpd(@PathVariable Long id){
        ModelAndView mav = new ModelAndView("form-impuestos");
        mav.addObject("impuesto", impuestosServicio.obtenerPorId(id));
        mav.addObject("action", "update");
        mav.addObject("listaIMpuestos", impuestosServicio.obtenerTodos());
        return mav;
    }

    @PostMapping("/create")
    public RedirectView create(Impuestos dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/impuestosCta");
        try{
            impuestosServicio.crear(dto);
            attributes.addFlashAttribute("exito", "Se ha creado correctamente el impuesto");
        }catch (Exception e){
            attributes.addFlashAttribute("impuesto", dto);
            attributes.addFlashAttribute("exception", e.getMessage());
            redirect.setUrl("/impuestoCta/form");
        }
        return redirect;
    }

    @PostMapping("update")
    public RedirectView update(Impuestos dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/impuestosCta");
        try{
            impuestosServicio.actualizar(dto);
            attributes.addFlashAttribute("exito", "Se ha actualizado correctamente el impuesto");
        }catch (Exception e){
            attributes.addFlashAttribute("impuesto", dto);
            attributes.addFlashAttribute("exception", e.getMessage());
            redirect.setUrl("/impuestoCta/form");
        }
        return redirect;
    }

    @GetMapping("/delete")
    public RedirectView delete(@PathVariable Long id, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/impuestosCta");
        impuestosServicio.eliminarPorId(id);
        attributes.addFlashAttribute("exito", "Se ha eliminado con exito el impuesto");
        return redirect;
    }









}
