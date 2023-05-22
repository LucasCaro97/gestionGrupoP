package com.grupop.gestion.Controladores;

import com.grupop.gestion.Entidades.CuentasDeBancoTarjeta;
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
@RequestMapping("/ctaBcoTja")
public class CuentasDeBancoTarjetaControlador {

    private final CuentasDeBancoTarjetaServicio cuentasDeBancoTarjetaServicio;
    private final EntidadBaseServicio entidadBaseServicio;
    private final TipoCuentaBancoServicio tipoCuentaBancoServicio;
    private final CuentasContablesServicio cuentasContablesServicio;

    @GetMapping
    public ModelAndView getAll(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("tabla-BancoTarjeta");
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);

        if(inputFlashMap!=null){ mav.addObject("exito", inputFlashMap.get("exito"));  }
        mav.addObject("listaBcoTja", cuentasDeBancoTarjetaServicio.obtenerTodos());
        return mav;
    }

    @GetMapping("/form")
    public ModelAndView getForm(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("form-BancoTarjeta");
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);

        if(inputFlashMap!=null){
            mav.addObject("bancoTarjeta", inputFlashMap.get("bancoTarjeta"));
            mav.addObject("exception", inputFlashMap.get("exception"));
        }else{
            mav.addObject("bancoTarjeta", new CuentasDeBancoTarjeta());
        }
        mav.addObject("listaProveedores", entidadBaseServicio.obtenerTodos());
        mav.addObject("listaTipo", tipoCuentaBancoServicio.obtenerTodos());
        mav.addObject("listaCuentas", cuentasContablesServicio.obtenerTodos());
        mav.addObject("action", "create");
        return mav;
    }


    @GetMapping("/form/{id}")
    public ModelAndView getFormUpd(@PathVariable Long id){
        ModelAndView mav = new ModelAndView("form-BancoTarjeta");
        mav.addObject("bancoTarjeta", cuentasDeBancoTarjetaServicio.obtenerPorId(id));
        mav.addObject("listaProveedores", entidadBaseServicio.obtenerTodos());
        mav.addObject("listaTipo", tipoCuentaBancoServicio.obtenerTodos());
        mav.addObject("listaCuentas", cuentasContablesServicio.obtenerTodos());
        mav.addObject("action", "update");
        return mav;
    }


    @PostMapping("/create")
    public RedirectView create(CuentasDeBancoTarjeta dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/ctaBcoTja");
        try{
            cuentasDeBancoTarjetaServicio.crear(dto);
            attributes.addFlashAttribute("exito", "Se ha creado la cuenta de banco/tarjeta correctamente");
        }catch(Exception e){
            attributes.addFlashAttribute("bancoTarjeta", dto);
            attributes.addFlashAttribute("exception", e.getMessage());
            redirect.setUrl("/ctaBcoTja/form");
        }
        return redirect;
    }

    @PostMapping("/update")
    public RedirectView update(CuentasDeBancoTarjeta dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/ctaBcoTja");
        try{
            cuentasDeBancoTarjetaServicio.actualizar(dto);
            attributes.addFlashAttribute("exito", "Se ha actualizado la cuenta de banco/tarjeta correctamente");
        }catch(Exception e){
            attributes.addFlashAttribute("bancoTarjeta", dto);
            attributes.addFlashAttribute("exception", e.getMessage());
            redirect.setUrl("/ctaBcoTja/form");
        }
        return redirect;
    }

    @GetMapping("delete/{id}")
    public RedirectView delete(@PathVariable Long id, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/ctaBcoTja");
        cuentasDeBancoTarjetaServicio.eliminarPorId(id);
        attributes.addFlashAttribute("exito", "Se ha eliminado correctamente la cuenta de banco/tarjeta");
        return redirect;
    }


}
