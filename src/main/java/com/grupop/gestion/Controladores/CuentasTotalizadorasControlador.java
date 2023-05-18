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
        mav.addObject("listaCuentas1112", cuentasContablesServicio.obtenerCuentas1112());
        mav.addObject("listaCuentas1113", cuentasContablesServicio.obtenerCuentas1113());
        mav.addObject("listaCuentas1121", cuentasContablesServicio.obtenerCuentas1121());
        mav.addObject("listaCuentas1122", cuentasContablesServicio.obtenerCuentas1122());
        mav.addObject("listaCuentas1131", cuentasContablesServicio.obtenerCuentas1131());
        mav.addObject("listaCuentas1132", cuentasContablesServicio.obtenerCuentas1132());
        mav.addObject("listaCuentas1133", cuentasContablesServicio.obtenerCuentas1133());
        mav.addObject("listaCuentas1134", cuentasContablesServicio.obtenerCuentas1134());
        mav.addObject("listaCuentas1141", cuentasContablesServicio.obtenerCuentas1141());
        mav.addObject("listaCuentas1142", cuentasContablesServicio.obtenerCuentas1142());
        mav.addObject("listaCuentas1143", cuentasContablesServicio.obtenerCuentas1143());
        mav.addObject("listaCuentas1144", cuentasContablesServicio.obtenerCuentas1144());
        mav.addObject("listaCuentas1211", cuentasContablesServicio.obtenerCuentas1211());
        mav.addObject("listaCuentas1212", cuentasContablesServicio.obtenerCuentas1212());
        mav.addObject("listaCuentas1221", cuentasContablesServicio.obtenerCuentas1221());
        mav.addObject("listaCuentas1222", cuentasContablesServicio.obtenerCuentas1222());
        mav.addObject("listaCuentas1223", cuentasContablesServicio.obtenerCuentas1223());
        mav.addObject("listaCuentas123", cuentasContablesServicio.obtenerCuentas123());
        mav.addObject("listaCuentas124", cuentasContablesServicio.obtenerCuentas124());
        mav.addObject("listaCuentas211", cuentasContablesServicio.obtenerCuentas211());
        mav.addObject("listaCuentas212", cuentasContablesServicio.obtenerCuentas212());
        mav.addObject("listaCuentas213", cuentasContablesServicio.obtenerCuentas213());
        mav.addObject("listaCuentas214", cuentasContablesServicio.obtenerCuentas214());
        mav.addObject("listaCuentas215", cuentasContablesServicio.obtenerCuentas215());
        mav.addObject("listaCuentas216", cuentasContablesServicio.obtenerCuentas216());
        mav.addObject("listaCuentas221", cuentasContablesServicio.obtenerCuentas221());
        mav.addObject("listaCuentas222", cuentasContablesServicio.obtenerCuentas222());
        mav.addObject("listaCuentas223", cuentasContablesServicio.obtenerCuentas223());
        mav.addObject("listaCuentas224", cuentasContablesServicio.obtenerCuentas224());
        mav.addObject("listaCuentas225", cuentasContablesServicio.obtenerCuentas225());
        mav.addObject("listaCuentas31", cuentasContablesServicio.obtenerCuentas31());
        mav.addObject("listaCuentas32", cuentasContablesServicio.obtenerCuentas32());
        mav.addObject("listaCuentas411", cuentasContablesServicio.obtenerCuentas411());
        mav.addObject("listaCuentas412", cuentasContablesServicio.obtenerCuentas412());
        mav.addObject("listaCuentas413", cuentasContablesServicio.obtenerCuentas413());
        mav.addObject("listaCuentas421", cuentasContablesServicio.obtenerCuentas421());
        mav.addObject("listaCuentas422", cuentasContablesServicio.obtenerCuentas422());
        mav.addObject("listaCuentas423", cuentasContablesServicio.obtenerCuentas423());
        mav.addObject("listaCuentas424", cuentasContablesServicio.obtenerCuentas424());
        mav.addObject("listaCuentas425", cuentasContablesServicio.obtenerCuentas425());
        mav.addObject("listaCuentas426", cuentasContablesServicio.obtenerCuentas426());
        mav.addObject("listaCuentas427", cuentasContablesServicio.obtenerCuentas427());
        mav.addObject("listaCuentas43", cuentasContablesServicio.obtenerCuentas43());
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
