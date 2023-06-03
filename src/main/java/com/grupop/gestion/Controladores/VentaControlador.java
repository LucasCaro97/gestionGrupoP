package com.grupop.gestion.Controladores;


import com.grupop.gestion.Entidades.Venta;
import com.grupop.gestion.Repositorios.ProductoRepo;
import com.grupop.gestion.Repositorios.VentaRepo;
import com.grupop.gestion.Servicios.*;
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
@RequestMapping("/ventas")
public class VentaControlador {

    private final VentaServicio ventaServicio;
    private final EntidadBaseServicio entidadBaseServicio;
    private final TalonarioServicio talonarioServicio;
    private final TipoComprobanteServicio tipoComprobanteServicio;
    private final SectorServicio sectorServicio;
    private final MonedaServicio monedaServicio;
    private final TipoIvaServicio tipoIvaServicio;
    private final ProductoServicio productoServicio;


    @GetMapping
    public ModelAndView getAll (HttpServletRequest request){
        ModelAndView mav = new ModelAndView("tabla-ventas");
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if(inputFlashMap!=null){ mav.addObject("exito", inputFlashMap.get("exito")); }
        mav.addObject("listaVentas", ventaServicio.obtenerTodas());
        return mav;
    }

    @GetMapping("/form")
    public ModelAndView getForm(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("form-ventas");
        Map<String,?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if(inputFlashMap!=null) {
        mav.addObject("exception", inputFlashMap.get("exception"));
        mav.addObject("venta", inputFlashMap.get("venta"));
        }else {
            mav.addObject("venta", new Venta());
        }
        mav.addObject("action", "create");
        mav.addObject("listaClientes", entidadBaseServicio.obtenerClientes());
        mav.addObject("listaTalonario", talonarioServicio.obtenerTodos() );
        mav.addObject("listaTipoComp", tipoComprobanteServicio.obtenerTodos());
        mav.addObject("listaSector", sectorServicio.obtenerTodos());
        mav.addObject("listaMoneda", monedaServicio.obtenerTodos());
        mav.addObject("listaIva", tipoIvaServicio.obtenerTodos());
        mav.addObject("listaProd", productoServicio.obtenerTodos());
        return mav;
    }

    @GetMapping("/form/{id}")
    public ModelAndView getFormUpd(@PathVariable Long id){
        ModelAndView mav = new ModelAndView("form-ventas");
        // agregar validacion para no modificar en caso de que posea algun cobro
        mav.addObject(ventaServicio.obtenerPorId(id));
        mav.addObject("action", "update");
        mav.addObject("listaClientes", entidadBaseServicio.obtenerClientes());
        mav.addObject("listaClientes", entidadBaseServicio.obtenerClientes());
        mav.addObject("listaTalonario", talonarioServicio.obtenerTodos() );
        mav.addObject("listaTipoComp", tipoComprobanteServicio.obtenerTodos());
        mav.addObject("listaSector", sectorServicio.obtenerTodos());
        mav.addObject("listaMoneda", monedaServicio.obtenerTodos());
        mav.addObject("listaIva", tipoIvaServicio.obtenerTodos());
        mav.addObject("listaProd", productoServicio.obtenerTodos());
        return mav;
    }

    @PostMapping("/create")
    public RedirectView create(Venta dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/ventas/form/" + ventaServicio.buscarUltimoId());
        try{
            ventaServicio.crear(dto);
            attributes.addFlashAttribute("exito", "Se ha generado el registro correctamente");
            attributes.addFlashAttribute("venta", dto);
        }catch (Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            attributes.addFlashAttribute("venta", dto);
            redirect.setUrl("/ventas/form");
        }
        return redirect;
    }

    @PostMapping("/update")
    public RedirectView update(Venta dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/ventas");
        try{
            ventaServicio.actualizar(dto);
            attributes.addFlashAttribute("exito", "Se ha actualizado correctamente el registro");
        }catch (Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            attributes.addFlashAttribute("venta", dto);
            redirect.setUrl("/ventas/form");
        }
        return redirect;
    }


    @GetMapping("/delete/{id}")
    public RedirectView delete(@PathVariable Long id, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/ventas");
        ventaServicio.eliminarPorId(id);
        attributes.addFlashAttribute("exito", "Se ha eliminado correctamente el registro");
        return redirect;
    }

}
