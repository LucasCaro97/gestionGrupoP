package com.grupop.gestion.Controladores;

import com.grupop.gestion.Entidades.EstadoCredito;
import com.grupop.gestion.Servicios.EstadoCreditoServicio;
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
@RequestMapping("/estadoCredito")
public class EstadoCreditoControlador {

    private final EstadoCreditoServicio estadoCreditoServicio;

    @GetMapping
    public ModelAndView getAll(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("tabla-estadoCredito");
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);

        if(inputFlashMap != null) mav.addObject("exito", inputFlashMap.get("exito"));
        mav.addObject("listaEstados", estadoCreditoServicio.obtenerTodos());
        return mav;
    }

    @GetMapping("/form")
    public ModelAndView getForm(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("form-estadoCredito");
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if(inputFlashMap!=null){
            mav.addObject("estado", inputFlashMap.get("estado"));
            mav.addObject("exception", inputFlashMap.get("exception"));
        }else{
            mav.addObject("estado", new EstadoCredito());
            mav.addObject("listaEstados", estadoCreditoServicio.obtenerTodos());
        }
        mav.addObject("action", "create");
        return mav;
    }

    @GetMapping("/form/{id}")
    public ModelAndView getFormUpdate(@PathVariable Long id){
        ModelAndView mav = new ModelAndView("form-estadoCredito");
        mav.addObject("estado", estadoCreditoServicio.obtenerPorId(id));
        mav.addObject("action", "update");
        return mav;
    }

    @PostMapping("/create")
    public RedirectView create(EstadoCredito dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/estadoCredito");
        try{
            estadoCreditoServicio.crear(dto);
            attributes.addFlashAttribute("exito", "Se ha cargado el registro correctamente");
        } catch (Exception e){
            attributes.addFlashAttribute("estado", dto);
            attributes.addFlashAttribute("exception", e.getMessage());
            redirect.setUrl("/estadoCredito/form");
        }
        return redirect;
    }

    @PostMapping("/update")
    public RedirectView update(EstadoCredito dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/estadoCredito");
        try{
            estadoCreditoServicio.actualizar(dto);
            attributes.addFlashAttribute("exito", "Se ha actualizado el registro correctamente");
        } catch (Exception e){
            attributes.addFlashAttribute("estado", dto);
            attributes.addFlashAttribute("excepion", e.getMessage());
            redirect.setUrl("/estadoCredito/form");
        }
        return redirect;
    }



    @GetMapping("/delete/{id}")
    public RedirectView delete(@PathVariable Long id, RedirectAttributes attributes) {
        RedirectView redirect = new RedirectView("/estadoCredito");
        estadoCreditoServicio.eliminarPorId(id);
        attributes.addFlashAttribute("exito", "Se ha eliminado correctamente el registro");
        return  redirect;
    }



}

