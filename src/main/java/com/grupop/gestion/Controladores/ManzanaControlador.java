package com.grupop.gestion.Controladores;


import com.grupop.gestion.DTO.ManzanaAltDTO;
import com.grupop.gestion.Entidades.Lote;
import com.grupop.gestion.Entidades.Manzana;
import com.grupop.gestion.Servicios.ManzanaServicio;
import com.grupop.gestion.Servicios.UrbanizacionServicio;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/manzana")
public class ManzanaControlador {

    private final ManzanaServicio manzanaServicio;
    private final UrbanizacionServicio urbanizacionServicio;

    @GetMapping
    public ModelAndView getAll(HttpServletRequest request, @Param("urbanizacion") Long urbanizacion){
        ModelAndView mav = new ModelAndView("tabla-manzana");
        Map<String,?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if(inputFlashMap!=null){ mav.addObject("exito", inputFlashMap.get("exito")); }
        mav.addObject("listaManzanas",manzanaServicio.obtenerTodos(urbanizacion));
        mav.addObject("listaUrbs", urbanizacionServicio.obtenerTodos());
        return mav;
    }

    @GetMapping("/form")
    public ModelAndView getForm(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("form-manzana");
        Map<String,?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if(inputFlashMap!=null){
            mav.addObject("manzana", inputFlashMap.get("manzana"));
            mav.addObject("exception", inputFlashMap.get("exception"));
        }else {
            mav.addObject("manzana", new Manzana());
        }
        mav.addObject("action", "create");
        mav.addObject("listaUrbs", urbanizacionServicio.obtenerTodos());
        return mav;
    }


    @GetMapping("/form/{id}")
    public ModelAndView getFormUpd(@PathVariable Long id){
        ModelAndView mav = new ModelAndView("form-manzana");
        mav.addObject("manzana", manzanaServicio.obtenerPorId(id));
        mav.addObject("action", "update");
        mav.addObject("listaUrbs", urbanizacionServicio.obtenerTodos());
        return mav;
    }

    @PostMapping("create")
    public RedirectView create(Manzana dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/manzana");
        try{
            manzanaServicio.crear(dto);
            attributes.addFlashAttribute("exito", "Se ha creado correctamente la manzana");
        }catch(Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            attributes.addFlashAttribute("manzana", dto);
            redirect.setUrl("/manzana/form");
        }
        return redirect;
    }

    @PostMapping("createRest")
    public ResponseEntity<String> createRest(@RequestBody ManzanaAltDTO dto, RedirectAttributes attributes){
        try {
            manzanaServicio.crear(dto);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Se creo el registro correctamente");
    }


    @PostMapping("update")
    public RedirectView update(Manzana dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/manzana");
        try{
            manzanaServicio.actualizar(dto);
            attributes.addFlashAttribute("exito", "Se ha actualizado correctamente la manzana");
        }catch(Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            attributes.addFlashAttribute("manzana", dto);
            redirect.setUrl("/manzana/form");
        }
        return redirect;
    }


    @GetMapping("/delete")
    public RedirectView delete(@PathVariable Long id, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/lote");
        manzanaServicio.eliminarPorId(id);
        attributes.addFlashAttribute("exito", "Se ha eliminado correctamente la lote");
        return redirect;
    }



}
