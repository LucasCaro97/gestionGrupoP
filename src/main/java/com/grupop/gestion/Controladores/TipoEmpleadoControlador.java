package com.grupop.gestion.Controladores;

import com.grupop.gestion.Entidades.TipoEmpleado;
import com.grupop.gestion.Servicios.TipoEmpleadoServicio;
import com.grupop.gestion.Servicios.TipoIvaServicio;
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
@RequestMapping("/tipoEmpleado")
public class TipoEmpleadoControlador {

//    private final TipoEmpleadoServicio tipoEmpleadoServicio;
//
//    @GetMapping
//    public ModelAndView getAll(HttpServletRequest request){
//        ModelAndView mav = new ModelAndView("tabla-tipoEmpleado");
//        Map<String,?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
//
//        if(inputFlashMap != null) {
//            mav.addObject("exito", "La operacion se ha realizado con exito");
//        }
//        mav.addObject("listaTipoEmpleado", tipoEmpleadoServicio.obtenerTodos());
//        return mav;
//    }
//
//    @GetMapping("/form")
//    public ModelAndView getForm(HttpServletRequest request){
//        ModelAndView mav = new ModelAndView("form-tipoEmpleado");
//        Map<String,?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
//
//        if(inputFlashMap != null) {
//            mav.addObject("tipoEmpleado", inputFlashMap.get("tipoEmpleado"));
//            mav.addObject("exception", inputFlashMap.get("exception"));
//        } else{
//            mav.addObject("tipoEmpleado", new TipoEmpleado());
//            mav.addObject("listaTipoEmpleado", tipoEmpleadoServicio.obtenerTodos());
//        }
//        mav.addObject("action", "create");
//        return mav;
//    }
//
//    @GetMapping("/form/{id}")
//    public ModelAndView getFormUpdate(@PathVariable Long id){
//        ModelAndView mav = new ModelAndView("form-tipoEmpleado");
//        mav.addObject("tipoEmpleado", TipoEmpleadoServicio.buscarPorId(id));
//        mav.addObject("action", "update");
//        mav.addObject("listTipoEmpleado", tipoEmpleadoServicio.obtenerTodos());
//        return mav;
//    }

//    @PostMapping("/create")
//    public RedirectView create(TipoEmpleado dto, RedirectAttributes attributes){
//        RedirectView redirect = new RedirectView("/tipoEmpleado");
//        try{
//        }
//    }


}
