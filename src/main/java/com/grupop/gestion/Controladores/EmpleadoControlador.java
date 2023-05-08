package com.grupop.gestion.Controladores;

import com.grupop.gestion.Entidades.Empleado;
import com.grupop.gestion.Entidades.TipoEmpleado;
import com.grupop.gestion.Servicios.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/empleado")
public class EmpleadoControlador {

    private final EmpleadoServicio empleadoServicio;
    private final TipoEmpleadoServicio tipoEmpleadoServicio;
    private final PuestoServicio puestoServicio;
    private final SectorServicio sectorServicio;
    private final DepartamentoServicio departamentoServicio;


    @GetMapping("/form")
    public ModelAndView getForm(HttpServletRequest request){
        System.out.println("Llegue a form del controlador de empleado");
        ModelAndView mav = new ModelAndView("form-empleado");
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        mav.addObject("entidad", inputFlashMap.get("entidad"));
        mav.addObject("listaTipo", tipoEmpleadoServicio.obtenerTodos());
        mav.addObject("listaPuesto", puestoServicio.obtenerTodos());
        mav.addObject("listaSector", sectorServicio.obtenerTodos());
        mav.addObject("listaDep", departamentoServicio.obtenerTodos());
        System.out.println("entidad: " + inputFlashMap.get("entidad"));
        return mav;
    }


    @PostMapping("/create")
    public RedirectView crear(Empleado dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/entidadBase");
        try{
            empleadoServicio.crear();
            attributes.addFlashAttribute("exito", "Se ha generado el empleado correctamente");
        }catch (Exception e){
            attributes.addFlashAttribute("empleado", dto);
            attributes.addFlashAttribute("exception", e.getMessage());
            redirect.setUrl("/empleado/form");
        }
        return redirect;
    }

}
