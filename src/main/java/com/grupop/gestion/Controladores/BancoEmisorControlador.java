package com.grupop.gestion.Controladores;

import com.grupop.gestion.Entidades.BancoEmisor;
import com.grupop.gestion.Servicios.BancoEmisorServicio;
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
@RequestMapping("/bancoEmisor")
public class BancoEmisorControlador {

    private final BancoEmisorServicio bancoEmisorServicio;


    @GetMapping
    public ModelAndView getAll(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("tabla-bancoEmisor");
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if(inputFlashMap != null) mav.addObject("exito", inputFlashMap.get("exito"));
        mav.addObject("listaBancosEmisores", bancoEmisorServicio.obtenerTodos() );
        return mav;
    }


    @GetMapping("/form")
    public ModelAndView getForm(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("form-bancoEmisor");
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if(inputFlashMap != null) {
            mav.addObject("exception", inputFlashMap.get("exception"));
            mav.addObject("bancoEmisor", inputFlashMap.get("bancoEmisor"));
        }else{
            mav.addObject("bancoEmisor", new BancoEmisor());
        }
        mav.addObject("action", "create");
        return mav;
    }

    @GetMapping("/form/{id}")
    public ModelAndView getFormUpd(@PathVariable Long id){
        ModelAndView mav = new ModelAndView("form-bancoEmisor");
        mav.addObject("action", "update");
        mav.addObject("bancoEmisor", bancoEmisorServicio.obtenerPorId(id));
        return mav;
    }

    @PostMapping("/create")
    public RedirectView create(BancoEmisor dto, RedirectAttributes attributes){
        RedirectView r = new RedirectView("/bancoEmisor");
        try{
            bancoEmisorServicio.crear(dto);
            attributes.addFlashAttribute("exito", "Se ha creado el registro correctamente");
        }catch (Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            attributes.addFlashAttribute("bancoEmisor", dto);
            r.setUrl("/bancoEmisor/form");
        }
        return r;
    }

    @PostMapping("/update")
    public RedirectView update(BancoEmisor dto, RedirectAttributes attributes){
        RedirectView r = new RedirectView("/bancoEmisor");
        try{
            bancoEmisorServicio.actualizar(dto);
            attributes.addFlashAttribute("exito", "Se ha actualizado el registro correctamente");
        }catch (Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            attributes.addFlashAttribute("bancoEmisor", dto);
            r.setUrl("/bancoEmisor/form");
        }
        return r;
    }

    @GetMapping("/delete/{id}")
    public RedirectView delete (@PathVariable Long id, RedirectAttributes attributes){
        RedirectView r = new RedirectView("/bancoEmisor");
        bancoEmisorServicio.eliminarPorId(id);
        attributes.addFlashAttribute("exito", "Se ha eliminado el registro correctamente");
        return  r;
    }

}
