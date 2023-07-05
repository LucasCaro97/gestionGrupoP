package com.grupop.gestion.Controladores;

import com.grupop.gestion.Entidades.PlanPago;
import com.grupop.gestion.Servicios.IndiceCacServicio;
import com.grupop.gestion.Servicios.PlanPagoServicio;
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

import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/planPago")
public class PlanPagoControlador {

    private final PlanPagoServicio planPagoServicio;
    private final IndiceCacServicio indiceCacServicio;

    @GetMapping
    public ModelAndView getAll(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("tabla-planPago");
        Map<String,?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if(inputFlashMap!=null){ mav.addObject("exito", inputFlashMap.get("exito")); }
        mav.addObject("listaPlanPago", planPagoServicio.obtenerTodos());
        return mav;
    }


    @GetMapping("/form")
    public ModelAndView getForm(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("form-planPago");
        Map<String,?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if(inputFlashMap!=null){
            mav.addObject("exception", inputFlashMap.get("exception"));
            mav.addObject("planPago", inputFlashMap.get("planPago"));
        }else{
            mav.addObject("planPago", new PlanPago());
        }
        mav.addObject("listaIndice", indiceCacServicio.obtenerTodos());
        mav.addObject("action", "create");
        return mav;
    }


    @GetMapping("/form/{id}")
    public ModelAndView getFormUpd(@PathVariable Long id){
        ModelAndView mav = new ModelAndView("form-planPago");
        mav.addObject("planPago", planPagoServicio.obtenerPorId(id));
        mav.addObject("listaIndice", indiceCacServicio.obtenerTodos());
        mav.addObject("action", "update");
        return mav;
    }

    @PostMapping("/create")
    public RedirectView create(PlanPago dto, RedirectAttributes attributes){
        RedirectView r = new RedirectView("/planPago");
        try{
            planPagoServicio.crear(dto);
            attributes.addFlashAttribute("exito", "Se ha creado el plan de pago correctamente");
        } catch (Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            attributes.addFlashAttribute("planPago", dto);
            r.setUrl("/planPago/form");
        }
        return r;
    }

    @PostMapping("/update")
    public RedirectView update(PlanPago dto, RedirectAttributes attributes){
        RedirectView r = new RedirectView("/planPago");
        try{
            planPagoServicio.actualizar(dto);
            attributes.addFlashAttribute("exito", "Se ha actualizado correctamente el plan de pago");
        }catch (Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            attributes.addFlashAttribute("planPago", dto);
            r.setUrl("/planPago/form");
        }
        return r;
    }

    @GetMapping("/delete/{id}")
    public RedirectView delete(@PathVariable Long id, RedirectAttributes attributes){
        RedirectView r = new RedirectView("/planPago");
        planPagoServicio.eliminarPorId(id);
        attributes.addFlashAttribute("exito", "Se ha eliminado el plan de pago correctamente");
        return r;
    }

    @GetMapping("/obtenerPlanPago/{id}")
    public ResponseEntity<PlanPago> obtenerPlanPagoJSON(@PathVariable Long id){
        return ResponseEntity.ok(planPagoServicio.obtenerPorId(id));
    }

}
