package com.grupop.gestion.Controladores;

import com.grupop.gestion.Entidades.Impuestos;
import com.grupop.gestion.Entidades.Producto;
import com.grupop.gestion.Servicios.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
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
@RequestMapping("/producto")
public class ProductoControlador {

    private final ProductoServicio productoServicio;
    private final TipoProductoServicio tipoProductoServicio;
    private final CuentasContablesServicio cuentasContablesServicio;
    private final ImpuestosServicio impuestosServicio;
    private final LoteServicio loteServicio;


    @GetMapping
    public ModelAndView getAll(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("tabla-producto");
        Map<String,?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if (inputFlashMap!=null){ mav.addObject("exito", inputFlashMap.get("exito")); }
        mav.addObject("listaProd", productoServicio.obtenerTodos());
        return mav;
    }

    @GetMapping("/form")
    public ModelAndView getForm(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("form-producto");
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);

        if (inputFlashMap != null) {
            mav.addObject("exception", inputFlashMap.get("exception"));
            mav.addObject("producto", inputFlashMap.get("producto"));
        }else{
            mav.addObject("producto", new Producto());
        }
        mav.addObject("listaTipoProd", tipoProductoServicio.obtenerTodos());
        mav.addObject("listaCuentas", cuentasContablesServicio.obtenerTodos());
        mav.addObject("listaImpuestos", impuestosServicio.obtenerTodos());
        mav.addObject("action", "create");
        return mav;
    }

    @GetMapping("/form/{id}")
    public ModelAndView getFormUpd(@PathVariable Long id){
        ModelAndView mav = new ModelAndView("form-producto");
        mav.addObject("producto", productoServicio.buscarPorId(id));
        mav.addObject("action", "update");
        mav.addObject("listaTipoProd", tipoProductoServicio.obtenerTodos());
        mav.addObject("listaCuentas", cuentasContablesServicio.obtenerTodos());
        mav.addObject("listaImpuestos", impuestosServicio.obtenerTodos());
        return mav;
    }

    @PostMapping("/create")
    public RedirectView create(Producto dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/producto");
        try{
            productoServicio.crear(dto);
            attributes.addFlashAttribute("exito", "Se ha creado el producto correctamente");
        }catch (Exception e){
            attributes.addFlashAttribute("producto", dto);
            attributes.addFlashAttribute("exception", e.getMessage());
            attributes.addFlashAttribute("etapa", "etapa2");
            System.out.println("Redireccionando al formulario");
        }

        return redirect;
    }

    @PostMapping("update")
    RedirectView update(Producto dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/producto");
        try {
            productoServicio.actualizar(dto);
            attributes.addFlashAttribute("exito", "Se ha actualizado el producto correctamente");
        }catch (Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            attributes.addFlashAttribute("producto", dto);
            redirect.setUrl("/producto/form");
        }
        return redirect;
    }
    @GetMapping("delete/{id}")
    RedirectView delete(@PathVariable Long id, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/producto");
        Producto p = productoServicio.buscarPorId(id);
        loteServicio.bajaProducto(p);
        productoServicio.eliminarPorId(id);
        attributes.addFlashAttribute("exito", "Se ha eliminado correctamente el producto");
        return redirect;
    }



}
