package com.grupop.gestion.Controladores;

import com.grupop.gestion.Entidades.EstadoLote;
import com.grupop.gestion.Entidades.Lote;
import com.grupop.gestion.Entidades.Urbanizacion;
import com.grupop.gestion.Servicios.EstadoLoteServicio;
import com.grupop.gestion.Servicios.LoteServicio;
import com.grupop.gestion.Servicios.ManzanaServicio;
import com.grupop.gestion.Servicios.UrbanizacionServicio;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
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
@RequestMapping("/lote")
public class LoteControlador {

    private final LoteServicio loteServicio;
    private final ManzanaServicio  manzanaServicio;
    private final UrbanizacionServicio urbanizacionServicio;
    private final EstadoLoteServicio estadoLoteServicio;

    @GetMapping
    public ModelAndView getAll(HttpServletRequest request,  @Param("urbanizacion") Long urbanizacion, @Param("manzana") Long manzana){
        ModelAndView mav = new ModelAndView("tabla-lote");
        Map<String,?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if(inputFlashMap!=null){ mav.addObject("exito", inputFlashMap.get("exito")); }
        mav.addObject("listaLotes",loteServicio.obtenerTodos(urbanizacion, manzana));
        mav.addObject("listaUrbs", urbanizacionServicio.obtenerTodos());
        mav.addObject("listaManzana", manzanaServicio.obtenerTodos());
        return mav;
    }

    @GetMapping("/form")
    public ModelAndView getForm(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("form-lote");
        Map<String,?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if(inputFlashMap!=null){
            mav.addObject("lote", inputFlashMap.get("lote"));
            mav.addObject("exception", inputFlashMap.get("exception"));
        }else {
            mav.addObject("lote", new Lote());
        }
        mav.addObject("action", "create");
        mav.addObject("listaUrbs", urbanizacionServicio.obtenerTodos());
        mav.addObject("listaManzanas", manzanaServicio.obtenerTodos());
        mav.addObject("listaEstadoLote", estadoLoteServicio.obtenerTodos());
        return mav;
    }


    @GetMapping("/form/{id}")
    public ModelAndView getFormUpd(@PathVariable Long id){
        ModelAndView mav = new ModelAndView("form-lote");
        mav.addObject("lote", loteServicio.obtenerPorId(id));
        mav.addObject("listaUrbs", urbanizacionServicio.obtenerTodos());
        mav.addObject("listaEstadoLote", estadoLoteServicio.obtenerTodos());
        mav.addObject("listaManzana", manzanaServicio.obtenerTodos());
        mav.addObject("action", "update");
        return mav;
    }

    @PostMapping("create")
    public RedirectView create(Lote dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/lote");
        try{
            loteServicio.crear(dto);
            attributes.addFlashAttribute("exito", "Se ha creado correctamente la lote");
        }catch(Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            attributes.addFlashAttribute("lote", dto);
            redirect.setUrl("/lote/form");
        }
        return redirect;
    }

    @PostMapping("update")
    public RedirectView update(Lote dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/lote");
        try{
            loteServicio.actualizar(dto);
            attributes.addFlashAttribute("exito", "Se ha actualizado correctamente la lote");
        }catch(Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            attributes.addFlashAttribute("lote", dto);
            redirect.setUrl("/lote/form");
        }
        return redirect;
    }


    @GetMapping("/delete/{id}")
    public RedirectView delete(@PathVariable Long id, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/lote");
        loteServicio.eliminarPorId(id);
        attributes.addFlashAttribute("exito", "Se ha eliminado correctamente la lote");
        return redirect;
    }


}
