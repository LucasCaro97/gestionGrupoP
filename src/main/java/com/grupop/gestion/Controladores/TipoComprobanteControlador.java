package com.grupop.gestion.Controladores;

import com.grupop.gestion.Entidades.TipoComprobante;
import com.grupop.gestion.Servicios.TipoComprobanteServicio;
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
@RequestMapping("/tipoComprobante")
public class TipoComprobanteControlador {

    private final TipoComprobanteServicio tipoComprobanteServicio;


    @GetMapping
    public ModelAndView getAll(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("tabla-tipoComprobante");
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if (inputFlashMap!=null){ mav.addObject("exito", inputFlashMap.get("exito")); }
        mav.addObject("listaTipoComp", tipoComprobanteServicio.obtenerTodos());
        return mav;
    }

    @GetMapping("/form")
    public ModelAndView getForm(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("form-tipoComprobante");
        Map<String,?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if (inputFlashMap!=null){
            mav.addObject("tipoComprob", inputFlashMap.get("tipoComprob"));
            mav.addObject("exception", inputFlashMap.get("exception"));
        }else{
            mav.addObject("tipoComprobante", new TipoComprobante());
        }
        mav.addObject("action", "create");
        mav.addObject("listaTipoComp", tipoComprobanteServicio.obtenerTodos());
        return mav;
    }


    @GetMapping("/form/{id}")
    public ModelAndView getForm(@PathVariable Long id){
        ModelAndView mav = new ModelAndView("form-tipoComprobante");
        mav.addObject("tipoComprobante", tipoComprobanteServicio.obtenerPorId(id));
        mav.addObject("listaTipoComp", tipoComprobanteServicio.obtenerTodos());
        mav.addObject("action", "update");
        return mav;
    }

    @PostMapping("/create")
    public RedirectView create(TipoComprobante dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/tipoComprobante");
        try{
            tipoComprobanteServicio.crear(dto);
            attributes.addFlashAttribute("exito", "Se ha creado con exito el tipo de comprobante");
        }catch(Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            attributes.addFlashAttribute("tipoComprobante", dto);
            redirect.setUrl("/tipoComprobante/form");
        }
        return redirect;
    }


    @PostMapping("/update")
    public RedirectView update(TipoComprobante dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/tipoComprobante");
        try{
            tipoComprobanteServicio.actualizar(dto);
            attributes.addFlashAttribute("exito", "Se ha actualizado correctamente el tipo de comprobante");
        } catch (Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            attributes.addFlashAttribute("tipoComprobante", dto);
            redirect.setUrl("/tipoComprobante/form");
        }
        return redirect;
    }

    @GetMapping("delete/{id}")
    public RedirectView delete(@PathVariable Long id, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/tipoComprobante");
        tipoComprobanteServicio.eliminarPorId(id);
        attributes.addFlashAttribute("exito", "Se ha eliminado correctamente el tipo de comprobante");
        return redirect;
    }


}
