package com.grupop.gestion.Controladores;

import com.grupop.gestion.Entidades.Departamento;
import com.grupop.gestion.Entidades.EntidadBase;
import com.grupop.gestion.Servicios.ClienteServicio;
import com.grupop.gestion.Servicios.EntidadBaseServicio;
import com.grupop.gestion.Servicios.TipoIvaServicio;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;
import java.util.jar.Attributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/entidadBase")
public class EntidadBaseControlador {

    private final EntidadBaseServicio entidadBaseServicio;
    private final TipoIvaServicio tipoIvaServicio;
    private final ClienteServicio clienteServicio;

    @GetMapping
    public ModelAndView getAll(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("tabla-entidadBase");
        Map<String,?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);

        if(inputFlashMap != null) mav.addObject("exito", inputFlashMap.get("exito"));
        mav.addObject("listaEntidad", entidadBaseServicio.obtenerTodos());
        return mav;
    }

    @GetMapping("/form")
    public ModelAndView getForm(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("form-entidadBase");
        Map<String,?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);

        if(inputFlashMap != null){
            mav.addObject("entidad", inputFlashMap.get("entidad"));
            mav.addObject("exception", inputFlashMap.get("exception"));
        }else {
            mav.addObject("entidad", new EntidadBase());
            mav.addObject("listaEntidad", entidadBaseServicio.obtenerTodos());
            mav.addObject("listaIva", tipoIvaServicio.obtenerTodos());
        }
        mav.addObject("action", "create");
        return mav;
    }

    @GetMapping("/form/{id}")
    public ModelAndView getFormUpdate(@PathVariable Long id){
        ModelAndView mav = new ModelAndView("form-EntidadBase");
        mav.addObject("entidad", entidadBaseServicio.buscarPorId(id));
        mav.addObject("action", "update");
        mav.addObject("listaEntidad", entidadBaseServicio.obtenerTodos());
        mav.addObject("listaIva", tipoIvaServicio.obtenerTodos());
        return mav;
    }
    @PostMapping("/create")
    public RedirectView create(EntidadBase dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/entidadBase");
        try{
            entidadBaseServicio.crear(dto);
            attributes.addFlashAttribute("exito", "La operacion se ha realizado con exito");
        } catch (IllegalArgumentException e){
            attributes.addFlashAttribute("entidadBase", dto);
            attributes.addFlashAttribute("exception", e.getMessage());
            redirect.setUrl("/entidadBase/form");
        }
        return redirect;
    }


    @PostMapping("/update")
    public RedirectView update(EntidadBase dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/entidadBase");

        try{
            entidadBaseServicio.actualizar(dto);
            attributes.addFlashAttribute("exito", "La operacion se ha realizado con exito");
        }catch(IllegalArgumentException e){
            attributes.addFlashAttribute("entidad", dto);
            attributes.addFlashAttribute("exception", e.getMessage());
            redirect.setUrl("/entidadBase/form");
        }
        return redirect;
    }

    @GetMapping("/delete/{id}")
    public RedirectView deleteById(@PathVariable Long id, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/entidadBase");
        entidadBaseServicio.eliminarPorId(id);
        attributes.addFlashAttribute("exito", "La operacion se ha realizado con exito");
        return redirect;
    }

    @GetMapping("/setClient/{id}")
    public RedirectView setClient(@PathVariable Long id, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/entidadBase");
        entidadBaseServicio.setCliente(id);
        attributes.addFlashAttribute("exito", "Se ha asignado como cliente a la entidad nro:" + id);
        return redirect;
    }

    @GetMapping("/setProv/{id}")
    public RedirectView setProv(@PathVariable Long id, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/entidadBase");
        entidadBaseServicio.setProv(id);
        attributes.addFlashAttribute("exito", "Se ha asignado como proveedor a la entidad nro:" + id);
        return redirect;
    }

    @GetMapping("/setEmp/{id}")
    public RedirectView setEmp(@PathVariable Long id, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/entidadBase");
        entidadBaseServicio.setEmp(id);
        attributes.addFlashAttribute("exito", "Se ha asignado como empleado a la entidad nro:" + id);
        return redirect;
    }

    @GetMapping("/setVend/{id}")
    public RedirectView setVend(@PathVariable Long id, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/entidadBase");
        entidadBaseServicio.setVend(id);
        attributes.addFlashAttribute("exito", "Se ha asignado como vendedor a la entidad nro:" + id);
        return redirect;
    }


}
