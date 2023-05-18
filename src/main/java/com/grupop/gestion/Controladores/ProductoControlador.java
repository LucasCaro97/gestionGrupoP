package com.grupop.gestion.Controladores;

import com.grupop.gestion.Entidades.Impuestos;
import com.grupop.gestion.Entidades.Producto;
import com.grupop.gestion.Servicios.CuentasContablesServicio;
import com.grupop.gestion.Servicios.ImpuestosServicio;
import com.grupop.gestion.Servicios.TipoProductoServicio;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
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
@RequestMapping("/producto")
public class ProductoControlador {

    private final TipoProductoServicio tipoProductoServicio;
    private final CuentasContablesServicio cuentasContablesServicio;
    private final ImpuestosServicio impuestosServicio;

    @GetMapping
    public ModelAndView getAll(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("form-producto");
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);

        if (inputFlashMap != null) {mav.addObject("exito", inputFlashMap.get("exito"));}
        mav.addObject("listaTipoProd", tipoProductoServicio.obtenerTodos());
        mav.addObject("listaCuentas", cuentasContablesServicio.obtenerTodos());
        mav.addObject("listaImpuestos", impuestosServicio.obtenerTodos());
        return mav;
    }

    @PostMapping("/create")
    public RedirectView create(Producto dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/producto");
        try{
            System.out.println("creando producto");
            attributes.addFlashAttribute("exito", "Se ha creado el producto correctamente");
        }catch (Exception e){
            attributes.addFlashAttribute("producto", dto);
            attributes.addFlashAttribute("exception", e.getMessage());
            attributes.addFlashAttribute("etapa", "etapa2");
            System.out.println("Redireccionando al formulario");
        }
        return redirect;
    }



}
