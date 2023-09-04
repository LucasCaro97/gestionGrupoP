package com.grupop.gestion.Controladores;

import com.grupop.gestion.Entidades.ChequeTipo;
import com.grupop.gestion.Servicios.ChequeTipoServicio;
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
@RequestMapping("/tipoCheque")
public class ChequeTipoControlador {

    private final ChequeTipoServicio chequeTipoServicio;

    @GetMapping
    public ModelAndView getAll(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("tabla-tipoCheque");
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if(inputFlashMap != null) mav.addObject("exito", inputFlashMap.get("exito"));
        mav.addObject("listaTipoCheque", chequeTipoServicio.obtenerTodos());
        return mav;
    }

    @GetMapping("/form")
    public ModelAndView getForm(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("form-tipoCheque");
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if(inputFlashMap != null){
            mav.addObject("exception", inputFlashMap.get("exception"));
            mav.addObject("tipoCheque", inputFlashMap.get("tipoCheque"));
        }else{
            mav.addObject("tipoCheque", new ChequeTipo());
        }
        mav.addObject("action", "create");
        return mav;
    }

    @GetMapping("/form/{id}")
    public ModelAndView getFormUpd(@PathVariable Long id){
        ModelAndView mav = new ModelAndView("form-tipoCheque");
        mav.addObject("tipoCheque", chequeTipoServicio.obtenerPorId(id));
        mav.addObject("action", "update");
        return mav;
    }

    @PostMapping("/create")
    public RedirectView create (ChequeTipo dto , RedirectAttributes attributes){
        RedirectView r = new RedirectView("/tipoCheque");
        try{
            chequeTipoServicio.crear(dto);
            attributes.addFlashAttribute("exito", "Se ha creado el registro correctamente");
        }catch (Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            attributes.addFlashAttribute("tipoCheque", dto);
            r.setUrl("/tipoCheque/form");
        }
        return r;
    }

    @PostMapping("/update")
    public RedirectView update(ChequeTipo dto, RedirectAttributes attributes){
        RedirectView r = new RedirectView("/tipoCheque");
        try{
            chequeTipoServicio.actualizar(dto);
            attributes.addFlashAttribute("exito", "Se ha actualizado el registro correctamente");
        }catch (Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            attributes.addFlashAttribute("tipoCheque", dto);
            r.setUrl("/tipoCheque/form");
        }
        return r;
    }

    @GetMapping("/delete/{id}")
    public RedirectView delete(@PathVariable Long id, RedirectAttributes attributes){
        RedirectView r = new RedirectView("/tipoCheque");
        chequeTipoServicio.eliminarPorId(id);
        attributes.addFlashAttribute("exito", "Se ha eliminado el registro correctamente");
        return r;
    }


}

