package com.grupop.gestion.Controladores;

import com.grupop.gestion.Entidades.Talonario;
import com.grupop.gestion.Servicios.TalonarioServicio;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.Banner;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/talonario")
public class TalonarioControlador {


    private final TalonarioServicio talonarioServicio;

    @GetMapping
    public ModelAndView getAll(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("tabla-talonario");
        Map<String,?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if(inputFlashMap!=null){ mav.addObject("exito", inputFlashMap.get("exito")); }
        mav.addObject("listaTalonarios", talonarioServicio.obtenerTodos());
        return mav;
    }


    @GetMapping("/form")
    public ModelAndView getForm(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("form-talonario");
        Map<String,?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if (inputFlashMap!=null){
            mav.addObject("exception", inputFlashMap.get("exception"));
            mav.addObject("talonario", inputFlashMap.get("talonario"));
        }else{
            mav.addObject("talonario", new Talonario());
        }
        mav.addObject("action", "create");
        mav.addObject("listaTalonarios", talonarioServicio.obtenerTodos());
        return mav;
    }

    @GetMapping("/form/{id}")
    public ModelAndView getFormUpd(@PathVariable Long id){
        ModelAndView mav = new ModelAndView("form-talonario");
        mav.addObject("talonario", talonarioServicio.obtenerPorId(id));
        mav.addObject("listaTalonarios", talonarioServicio.obtenerTodos());
        mav.addObject("action", "update");
        return mav;
    }

    @PostMapping("/create")
    public RedirectView create(Talonario dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/talonario");
        try{
            talonarioServicio.crear(dto);
            attributes.addFlashAttribute("exito", "Se ha creado correctamente el talonario");
        }catch (Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            attributes.addFlashAttribute("talonario", dto);
            redirect.setUrl("/talonario/form");
        }
        return redirect;
    }

    @PostMapping("/update")
    public RedirectView update(Talonario dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/talonario");
        try {
            talonarioServicio.actualizar(dto);
            attributes.addFlashAttribute("exito", "Se ha actualizado correctamente el talonario");
        } catch (Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            attributes.addFlashAttribute("talonario", dto);
            redirect.setUrl("/talonario/form");
        }
        return redirect;
    }


    @GetMapping("/delete/{id}")
    public RedirectView delete(@PathVariable Long id, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/talonario");
        talonarioServicio.eliminarPorId(id);
        attributes.addFlashAttribute("exito", "Se ha eliminaro correcamente el talonario");
        return redirect;
    }

    @GetMapping("/obtenerNroComprobante/{id}")
    public ResponseEntity<String> obtenerNroComprobante(@PathVariable Long id){
        System.out.println(talonarioServicio.obtenerNroComprobante(id));
        return ResponseEntity.ok(talonarioServicio.obtenerNroComprobante(id));
    }


}
