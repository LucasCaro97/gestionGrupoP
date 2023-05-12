package com.grupop.gestion.Controladores;

import com.grupop.gestion.Entidades.TipoCuentaBanco;
import com.grupop.gestion.Servicios.TipoCuentaBancoServicio;
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
@RequestMapping("/tipoCuentaBanco")
public class TipoCuentaBancoControlador {

    private final TipoCuentaBancoServicio tipoCuentaBancoServicio;

    @GetMapping
    public ModelAndView getAll(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("tabla-tipoCuentaBanco");
        Map<String,?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);

        if(inputFlashMap != null) { mav.addObject("exito", inputFlashMap.get("exito")); }
        mav.addObject("listaTipoCuentaBanco", tipoCuentaBancoServicio.obtenerTodos());
        return mav;
    }

    @GetMapping("/form")
    public ModelAndView getForm(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("form-tipoCuentaBanco");
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if(inputFlashMap!=null){
            mav.addObject("tipoCuentaBanco", inputFlashMap.get("tipoCuentaBanco"));
            mav.addObject("exception", inputFlashMap.get("exception"));
        }else{
            mav.addObject("tipoCuentaBanco", new TipoCuentaBanco());
            mav.addObject("listaTipoCuentaBanco", tipoCuentaBancoServicio.obtenerTodos());
        }
        mav.addObject("action", "create");
        return mav;
    }

    @GetMapping("/form/{id}")
    public ModelAndView getFormUpd(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("form-tipoCuentaBanco");
        mav.addObject("tipoCuentaBanco", tipoCuentaBancoServicio.obtenerPorId(id));
        mav.addObject("action", "update");
        mav.addObject("listaTipoCuentaBanco", tipoCuentaBancoServicio.obtenerTodos());
        return mav;
    }

    @PostMapping("/create")
    public RedirectView create(TipoCuentaBanco dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/tipoCuentaBanco");
        try{
            tipoCuentaBancoServicio.crear(dto);
            attributes.addFlashAttribute("exito", "Se ha creado el tipo de cuenta/banco correctamente");
        }catch(Exception e){
            attributes.addFlashAttribute("tipoCuentaBanco", dto);
            attributes.addFlashAttribute("exception", e.getMessage());
            redirect.setUrl("/tipoCuentaBanco/form");
        }
        return redirect;
    }

    @PostMapping("/update")
    public RedirectView update(TipoCuentaBanco dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/tipoCuentaBanco");
        try{
            tipoCuentaBancoServicio.actualizar(dto);
            attributes.addFlashAttribute("exito", "Se ha actualizado el tipo de cuenta/banco correctamente");
        }catch(Exception e){
            attributes.addFlashAttribute("tipoCuentaBanco", dto);
            attributes.addFlashAttribute("exception", e.getMessage());
            redirect.setUrl("/tipoCuentaBanco/form");
        }
        return redirect;
    }

    @PostMapping("/delete/{id}")
    public RedirectView delete(@PathVariable Long id, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/tipoCuentaBanco");
        tipoCuentaBancoServicio.eliminarPorId(id);
        attributes.addFlashAttribute("exito", "Se ha eliminado el registro correctamente");
        return redirect;
    }



}
