package com.grupop.gestion.Controladores;

import com.grupop.gestion.Entidades.Puesto;
import com.grupop.gestion.Entidades.Sector;
import com.grupop.gestion.Servicios.PuestoServicio;
import com.grupop.gestion.Servicios.SectorServicio;
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
@RequestMapping("/sector")
public class SectorControlador {

    private final SectorServicio sectorServicio;

    @GetMapping
    public ModelAndView getAll(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("tabla-sector");
        Map<String,?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);

        if(inputFlashMap != null) mav.addObject("exito", inputFlashMap.get("exito"));
        mav.addObject("listaSector", sectorServicio.obtenerTodos());
        return mav;
    }

    @GetMapping("/form")
    public ModelAndView getForm(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("form-sector");
        Map<String,?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);

        if(inputFlashMap != null){
            mav.addObject("sector", inputFlashMap.get("sector"));
            mav.addObject("exception", inputFlashMap.get("exception"));
        }else {
            mav.addObject("sector", new Sector());
            mav.addObject("listaSector", sectorServicio.obtenerTodos());
        }
        mav.addObject("action", "create");
        return mav;
    }

    @GetMapping("/form/{id}")
    public ModelAndView getFormUpdate(@PathVariable Long id){
        ModelAndView mav = new ModelAndView("form-sector");
        mav.addObject("puesto", sectorServicio.buscarPorId(id));
        mav.addObject("action", "update");
        mav.addObject("listaSector", sectorServicio.obtenerTodos());
        return mav;
    }

    @PostMapping("/create")
    public RedirectView create(Sector dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/sector");
        try{
            sectorServicio.crear(dto);
            attributes.addFlashAttribute("exito", "La operacion se ha realizado con exito");
        } catch (IllegalArgumentException e){
            attributes.addFlashAttribute("sector", dto);
            attributes.addFlashAttribute("exception", e.getMessage());
            redirect.setUrl("/sector/form");
        }
        return redirect;
    }

    @PostMapping("/update")
    public RedirectView update(Sector dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/sector");

        try{
            sectorServicio.actualizar(dto);
            attributes.addFlashAttribute("exito", "La operacion se ha realizado con exito");
        }catch(IllegalArgumentException e){
            attributes.addFlashAttribute("sector", dto);
            attributes.addFlashAttribute("exception", e.getMessage());
            redirect.setUrl("/sector/form");
        }
        return redirect;
    }

    @GetMapping("/delete/{id}")
    public RedirectView deleteById(@PathVariable Long id, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/sector");
        sectorServicio.eliminarPorId(id);
        attributes.addFlashAttribute("exito", "La operacion se ha realizado con exito");
        return redirect;
    }


}
