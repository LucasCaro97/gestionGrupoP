package com.grupop.gestion.Controladores;

import com.grupop.gestion.Entidades.IndiceCAC;
import com.grupop.gestion.Servicios.IndiceCacServicio;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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

import java.math.BigDecimal;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("indiceCac")
public class IndiceCacControlador {

    private final IndiceCacServicio indiceCacServicio;

    @GetMapping
    public ModelAndView getAll(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("tabla-indiceCac");
        Map<String,?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if(inputFlashMap!=null) mav.addObject("exito", inputFlashMap.get("exito"));
        mav.addObject("listaIndices", indiceCacServicio.obtenerTodos());
        return mav;
    }


    @GetMapping("/form")
    public ModelAndView getForm(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("form-indiceCac");
        Map<String,?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if(inputFlashMap!=null) {
            mav.addObject("exception", inputFlashMap.get("exception"));
            mav.addObject("indiceCac", inputFlashMap.get("indice"));
        }else{
            mav.addObject("indiceCac", new IndiceCAC());
        }
        mav.addObject("action", "create");
        mav.addObject("listaIndices", indiceCacServicio.obtenerTodos());
        return mav;
    }

    @GetMapping("/form/{id}")
    public ModelAndView getFormUpd(@PathVariable Long id){
        ModelAndView mav = new ModelAndView("form-indiceCac");
        mav.addObject("indiceCac", indiceCacServicio.obtenerPorId(id));
        mav.addObject("action", "update");
        return mav;
    }

    @PostMapping("/create")
    public RedirectView create (IndiceCAC dto, RedirectAttributes attributes){
        RedirectView r = new RedirectView("/indiceCac");
        try{
            indiceCacServicio.crear(dto);
            attributes.addFlashAttribute("exito", "Se ha creado el indice correctamente");
        }catch(Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            attributes.addFlashAttribute("indiceCac", dto);
            r.setUrl("/indiceCac/form");
        }
        return r;
    }

    @PostMapping("/update")
    public RedirectView update(IndiceCAC dto, RedirectAttributes attributes){
        RedirectView r = new RedirectView("/indiceCac");
        try{
            indiceCacServicio.actualizar(dto);
            attributes.addFlashAttribute("exito", "Se ha actualizado correctamente el registro");
        }catch (Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            attributes.addFlashAttribute("indiceCac", dto);
            r.setUrl("/indiceCac/form");
        }
        return r;
    }

    @GetMapping("/delete/{id}")
    public RedirectView delete(@PathVariable Long id, RedirectAttributes attributes){
        RedirectView r = new RedirectView("/indiceCac");
        indiceCacServicio.eliminarPorId(id);
        attributes.addFlashAttribute("exito", "Se ha eliminado el registro correctamente");
        return r;
    }


    @GetMapping("/obtenerIndice/{mes}/{anio}")
    public ResponseEntity<BigDecimal> obtenerIndiceBase(@PathVariable Integer mes, @PathVariable Integer anio){
       return ResponseEntity.ok(indiceCacServicio.obtenerIndiceBase(mes, anio));
    }


}
