package com.grupop.gestion.Controladores;

import com.grupop.gestion.Entidades.ContadorTotalizadora;
import com.grupop.gestion.Entidades.CuentasTotalizadoras;
import com.grupop.gestion.Servicios.ContadorTotalizadoraServicio;
import com.grupop.gestion.Servicios.CuentasContablesServicio;
import com.grupop.gestion.Servicios.CuentasTotalizadorasServicio;
import com.grupop.gestion.Servicios.TipoTotalizadoraServicio;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.Banner;
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
@RequestMapping("/ctaTot")
public class CuentasTotalizadorasControlador {

    private final CuentasTotalizadorasServicio cuentasTotalizadorasServicio;
    private final TipoTotalizadoraServicio tipoTotalizadoraServicio;
    private final ContadorTotalizadoraServicio contadorTotalizadoraServicio;
    private final CuentasContablesServicio cuentasContablesServicio;


    @GetMapping()
    public ModelAndView getAll(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("tabla-cuentasTotalizadoras");
        Map<String,?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);

        if(inputFlashMap!=null){ mav.addObject("exito", inputFlashMap.get("exito"));       }
        mav.addObject("listaCuentasTot", cuentasTotalizadorasServicio.obtenerTodos());
        mav.addObject("listaCuentas1111", cuentasContablesServicio.obtenerCuentas1111());
        return  mav;
    }


    @GetMapping("/form")
    public ModelAndView getForm(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("form-cuentasTotalizadoras");
        Map<String,?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if(inputFlashMap!=null){
            mav.addObject("ctaTot", inputFlashMap.get("ctaTot"));
            mav.addObject("exception", inputFlashMap.get("exception"));
        }else{
            mav.addObject("ctaTot", new CuentasTotalizadoras());
        }
        mav.addObject("action", "create");
        mav.addObject("listaTot", cuentasTotalizadorasServicio.obtenerTodos());
        mav.addObject("listaTipoTot", tipoTotalizadoraServicio.obtenerTodos());
        mav.addObject("listaNivel", contadorTotalizadoraServicio.obtenerTodos());
        return mav;
    }

    @GetMapping("/form/{id}")
    public ModelAndView getFormUpd(@PathVariable Long id){
        ModelAndView mav = new ModelAndView("/form-cuentasTotalizadoras");
        mav.addObject("ctaTot",cuentasTotalizadorasServicio.obtenerPorId(id));
        mav.addObject("action", "update");
        mav.addObject("listaTot", cuentasTotalizadorasServicio.obtenerTodos());
        mav.addObject("listaTipoTot", tipoTotalizadoraServicio.obtenerTodos());
        mav.addObject("listaNivel", contadorTotalizadoraServicio.obtenerTodos());
        return mav;
    }

    @PostMapping("/create")
    public RedirectView create(CuentasTotalizadoras dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/ctaTot");
        try{
            cuentasTotalizadorasServicio.crear(dto);
            attributes.addFlashAttribute("exito", "Se ha creado la cuenta totalizadora correctamente");
        }catch(Exception e){
            System.out.println("Entre al catch");
            attributes.addFlashAttribute("ctaTot", dto);
            attributes.addFlashAttribute("exception", e.getMessage());
            redirect.setUrl("/ctaTot/form");
        }
        return redirect;
    }

    @PostMapping("/update")
    public RedirectView update(CuentasTotalizadoras dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/ctaTot");
        try{
            cuentasTotalizadorasServicio.actualizar(dto);
            attributes.addFlashAttribute("exito", "Se ha actualizado la cuenta totalizadora correctamente");
        }catch(Exception e){
            attributes.addFlashAttribute("ctaTot", dto);
            attributes.addFlashAttribute("exception", e.getMessage());
            redirect.setUrl("/ctaTot/form");
            System.out.println(e.getMessage());
        }
        return redirect;

    }

    @GetMapping("/delete/{id}")
    public RedirectView delete(@PathVariable Long id, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/ctaTot");
        cuentasTotalizadorasServicio.eliminarPorId(id);
        attributes.addFlashAttribute("exito", "Se ha eliminado correctamente la cuenta totalizadora");
        return redirect;
    }

}
