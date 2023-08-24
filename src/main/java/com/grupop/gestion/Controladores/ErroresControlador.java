package com.grupop.gestion.Controladores;

import com.grupop.gestion.Entidades.Errores;
import com.grupop.gestion.Servicios.ErroresServicio;
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
@RequestMapping("/errores")
public class ErroresControlador {

    private final ErroresServicio erroresServicio;

    @GetMapping
    public ModelAndView getAll(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("tabla-errores");
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);

        if(inputFlashMap!=null) mav.addObject("exito", inputFlashMap.get("exito"));
        mav.addObject("listaErrores", erroresServicio.obtenerTodos());
        return mav;
    }

    @GetMapping("/form")
    public ModelAndView getForm(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("form-errores");
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if(inputFlashMap!=null){
            mav.addObject("exception", inputFlashMap.get("exception"));
            mav.addObject("error", inputFlashMap.get("error"));
        }else{
            mav.addObject("error", new Errores());
        }
        mav.addObject("action", "create");
        return mav;
    }

    @GetMapping("/form/{id}")
    public ModelAndView getFormUpd(@PathVariable Long id){
        ModelAndView mav = new ModelAndView("form-errores");
        mav.addObject("error", erroresServicio.obtenerError(id));
        mav.addObject("action", "update");
        return mav;
    }

    @PostMapping("/create")
    public RedirectView create(Errores dto, RedirectAttributes attributes){
        RedirectView r = new RedirectView("/errores");
        try {
            erroresServicio.crear(dto);
            attributes.addFlashAttribute("exito", "Se ha creado el registro correctamente");
        }catch (Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            attributes.addFlashAttribute("error", dto);
            r.setUrl("/errores/form");
        }
        return r;
    }

    @PostMapping("/update")
    public RedirectView update(Errores dto, RedirectAttributes attributes){
        RedirectView r = new RedirectView("/errores");
        try{
            erroresServicio.actualizar(dto);
            attributes.addFlashAttribute("exito", "Se ha actualizado el registro correctamente");
        }catch(Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            attributes.addFlashAttribute("error", dto);
            r.setUrl("/errores/form");
        }
        return r;
    }

    @GetMapping("/delete/{id}")
    public RedirectView delete(@PathVariable Long id, RedirectAttributes attributes){
        RedirectView r = new RedirectView("/errores");
        erroresServicio.eliminarPorId(id);
        attributes.addFlashAttribute("exito", "Se ha eliminado el registro correctamente");
        return r;
    }


}
