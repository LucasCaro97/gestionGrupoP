package com.grupop.gestion.Controladores;

import com.grupop.gestion.Entidades.Moneda;
import com.grupop.gestion.Entidades.TipoProducto;
import com.grupop.gestion.Servicios.TipoProductoServicio;
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
@RequestMapping("/tipoProducto")
public class TipoProductoControlador {

    private final TipoProductoServicio tipoProductoServicio;

    @GetMapping
    public ModelAndView getAll(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("tabla-tipoProducto");
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);

        if(inputFlashMap != null) mav.addObject("exito", inputFlashMap.get("exito"));
        mav.addObject("listaTipoProducto", tipoProductoServicio.obtenerTodos());
        return mav;
    }

    @GetMapping("/form")
    public ModelAndView getForm(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("form-tipoProducto");
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if(inputFlashMap!=null){
            mav.addObject("tipoProducto", inputFlashMap.get("tipoProducto"));
            mav.addObject("exception", inputFlashMap.get("exception"));
        }else{
            mav.addObject("tipoProducto", new Moneda());
            mav.addObject("listaTipoProducto", tipoProductoServicio.obtenerTodos());
        }
        mav.addObject("action", "create");
        return mav;
    }

    @GetMapping("/form/{id}")
    public ModelAndView getFormUpdate(@PathVariable Long id){
        ModelAndView mav = new ModelAndView("form-tipoProducto");
        mav.addObject("tipoProducto", tipoProductoServicio.obtenerPorId(id));
        mav.addObject("listaTipoProducto", tipoProductoServicio.obtenerTodos());
        mav.addObject("action", "update");
        return mav;
    }

    @PostMapping("/create")
    public RedirectView create(TipoProducto dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/tipoProducto");
        try{
            tipoProductoServicio.crear(dto);
            attributes.addFlashAttribute("exito", "Se ha cargado el tipo de producto correctamente");
        } catch (Exception e){
            attributes.addFlashAttribute("tipoProducto", dto);
            attributes.addFlashAttribute("exception", e.getMessage());
            redirect.setUrl("/tipoProducto/form");
        }
        return redirect;
    }

    @PostMapping("/update")
    public RedirectView update(TipoProducto dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/tipoProducto");
        try{
            tipoProductoServicio.actualizar(dto);
            attributes.addFlashAttribute("exito", "Se ha actualizado el tipo de producto correctamente");
        } catch (Exception e){
            attributes.addFlashAttribute("tipoProducto", dto);
            attributes.addFlashAttribute("excepion", e.getMessage());
            redirect.setUrl("/tipoProducto/form");
        }
        return redirect;
    }



    @GetMapping("/delete/{id}")
    public RedirectView delete(@PathVariable Long id, RedirectAttributes attributes) {
        RedirectView redirect = new RedirectView("/tipoProducto");
        tipoProductoServicio.eliminarPorId(id);
        attributes.addFlashAttribute("exito", "Se ha eliminado el tipo de producto correctamente");
        return  redirect;
    }


}
