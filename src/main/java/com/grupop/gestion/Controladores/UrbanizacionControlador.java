package com.grupop.gestion.Controladores;

import com.grupop.gestion.DTO.ManzanaDto;
import com.grupop.gestion.Entidades.CuentasContables;
import com.grupop.gestion.Entidades.Manzana;
import com.grupop.gestion.Entidades.Urbanizacion;
import com.grupop.gestion.Repositorios.ManzanaRepo;
import com.grupop.gestion.Servicios.CuentasContablesServicio;
import com.grupop.gestion.Servicios.LoteServicio;
import com.grupop.gestion.Servicios.ManzanaServicio;
import com.grupop.gestion.Servicios.UrbanizacionServicio;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Controller
@RequiredArgsConstructor
@RequestMapping("/urbanizacion")
public class UrbanizacionControlador {

    private final UrbanizacionServicio urbanizacionServicio;
    private final ManzanaServicio manzanaServicio;
    private final LoteServicio loteServicio;
    private final ManzanaRepo manzanaRepo;
    private final CuentasContablesServicio cuentasContablesServicio;

    @GetMapping
    public ModelAndView getAll(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("tabla-urbanizacion");
        Map<String,?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if(inputFlashMap!=null){ mav.addObject("exito", inputFlashMap.get("exito")); }
        mav.addObject("listaUrbs",urbanizacionServicio.obtenerTodos());
        return mav;
    }

    @GetMapping("/form")
    public ModelAndView getForm(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("form-urbanizacion");
        Map<String,?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if(inputFlashMap!=null){
            mav.addObject("urbanizacion", inputFlashMap.get("urbanizacion"));
            mav.addObject("exception", inputFlashMap.get("exception"));
        }else {
            mav.addObject("urbanizacion", new Urbanizacion());
        }
        mav.addObject("action", "create");
        mav.addObject("listaCuentas", cuentasContablesServicio.obtenerTodos());
        return mav;
    }


    @GetMapping("/form/{id}")
    public ModelAndView getFormUpd(@PathVariable Long id){
        ModelAndView mav = new ModelAndView("form-urbanizacion");
        mav.addObject("urbanizacion", urbanizacionServicio.obtenerPorId(id));
        mav.addObject("listaManzanas", manzanaServicio.obtenerPorUrbanizacion(id));
        mav.addObject("listaLotes", loteServicio.obtenerPorUrbanizacion(id));
        mav.addObject("listaCuentas", cuentasContablesServicio.obtenerTodos());
        mav.addObject("action", "update");
        mav.addObject("id", id);
        return mav;
    }

    @PostMapping("create")
    public RedirectView create(Urbanizacion dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/urbanizacion");
        try{
            urbanizacionServicio.crear(dto);
            attributes.addFlashAttribute("exito", "Se ha creado correctamente la urbanizacion");
        }catch(Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            attributes.addFlashAttribute("urbanizacion", dto);
            attributes.addFlashAttribute("id", dto.getId());
            redirect.setUrl("/urbanizacion/form");
        }
        return redirect;
    }

    @PostMapping("update")
    public RedirectView update(Urbanizacion dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/urbanizacion");
        try{
            urbanizacionServicio.actualizar(dto);
            attributes.addFlashAttribute("exito", "Se ha actualizado correctamente la urbanizacion");
        }catch(Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            attributes.addFlashAttribute("urbanizacion", dto);
            attributes.addFlashAttribute("id", dto.getId());
            redirect.setUrl("/urbanizacion/form");
        }
        return redirect;
    }


    @GetMapping("/delete")
    public RedirectView delete(@PathVariable Long id, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/urbanizacion");
        urbanizacionServicio.eliminarPorId(id);
        attributes.addFlashAttribute("exito", "Se ha eliminado correctamente la urbanizacion");
        return redirect;
    }

    @GetMapping("/obtenerManzanasPorId/{id}")
    public ResponseEntity<List<Manzana>> obtenerManzanasDeLaUrbanizacion(@PathVariable Long id){
        System.out.println(manzanaRepo.findAll());
        return ResponseEntity.ok(manzanaRepo.obtenerPorUrb(id));
    }

    @GetMapping("/obtenerLinkMapa/{idUrb}")
    public ResponseEntity<String> obtenerLinkMapa(@PathVariable Long idUrb){
        return ResponseEntity.ok(urbanizacionServicio.obtenerLinkMapa(idUrb));
    }

    @GetMapping("/generarLotes/{id}")
    public ModelAndView getFormGenerarLotesPorCantidad(@PathVariable Long id){
        ModelAndView mav = new ModelAndView("form-generadorLotes");
        mav.addObject("urbanizacion", urbanizacionServicio.obtenerPorId(id));
        mav.addObject("listaManzana", manzanaServicio.obtenerPorUrbanizacion(id));
        return mav;
    }





}
