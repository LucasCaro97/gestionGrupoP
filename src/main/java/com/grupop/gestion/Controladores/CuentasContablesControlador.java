package com.grupop.gestion.Controladores;

import com.grupop.gestion.Entidades.CuentasContables;
import com.grupop.gestion.Repositorios.CuentasContablesRepo;
import com.grupop.gestion.Servicios.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cuentas")
public class CuentasContablesControlador {

    private final CuentasContablesServicio cuentasContablesServicio;
    private final ClasifiacionCtaServicio clasifiacionCtaServicio;
    private final MonedaServicio monedaServicio;
    private final ImpuestosServicio impuestosServicio;
    private final CuentasTotalizadorasServicio cuentasTotalizadorasServicio;

    @GetMapping
    public ModelAndView getAll(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("tabla-cuentasContables");
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);

        if (inputFlashMap != null) {
            mav.addObject("exito", inputFlashMap.get("exito"));
        }
        mav.addObject("listaCuentas", cuentasContablesServicio.obtenerTodos());
        return mav;
    }

    @GetMapping("/form")
    public ModelAndView getForm(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("form-cuentasContables");
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if (inputFlashMap != null) {
            mav.addObject("exception", inputFlashMap.get("exception"));
            mav.addObject("cuenta", inputFlashMap.get("cuenta"));
        } else {
            mav.addObject("cuenta", new CuentasContables());
        }
        mav.addObject("action", "create");
        mav.addObject("listaCuentas", cuentasContablesServicio.obtenerTodos());
        mav.addObject("listaClasif", clasifiacionCtaServicio.obtenerTodos());
        mav.addObject("listaMonedas", monedaServicio.obtenerTodos());
        mav.addObject("listaImpuestos", impuestosServicio.obtenerTodos());
        mav.addObject("listaTotalizadoras", cuentasTotalizadorasServicio.obtenerTodos());
        return mav;
    }

    @GetMapping("/form/{id}")
    public ModelAndView getFormUpd(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("form-cuentasContables");
        mav.addObject("cuenta", cuentasContablesServicio.obtenerPorid(id));
        mav.addObject("action", "update");
        mav.addObject("listaCuentas", cuentasContablesServicio.obtenerTodos());
        mav.addObject("listaClasif", clasifiacionCtaServicio.obtenerTodos());
        mav.addObject("listaMonedas", monedaServicio.obtenerTodos());
        mav.addObject("listaImpuestos", impuestosServicio.obtenerTodos());
        mav.addObject("listaTotalizadoras", cuentasTotalizadorasServicio.obtenerTodos());
        return mav;
    }

    @PostMapping("/create")
    public RedirectView create(CuentasContables dto, RedirectAttributes attributes) {
        RedirectView redirect = new RedirectView("/cuentas");
        try {
            cuentasContablesServicio.crear(dto);
            attributes.addFlashAttribute("exito", "Se ha creado correctamente la cuenta");
        } catch (Exception e) {
            attributes.addFlashAttribute("exception", e.getMessage());
            attributes.addFlashAttribute("cuenta", dto);
            redirect.setUrl("/cuentas/form");
        }
        return redirect;
    }

    @PostMapping("/update")
    public RedirectView update(CuentasContables dto, RedirectAttributes attributes) {
        RedirectView redirect = new RedirectView("/cuentas");
        try {
            cuentasContablesServicio.actualizar(dto);
            attributes.addFlashAttribute("exito", "Se ha creado correctamente la cuenta");
        } catch (Exception e) {
            attributes.addFlashAttribute("exception", e.getMessage());
            attributes.addFlashAttribute("cuenta", dto);
            redirect.setUrl("/cuentas/form");
        }
        return redirect;
    }

    @GetMapping("/delete")
    public RedirectView delete(@PathVariable Long id, RedirectAttributes attributes) {
        RedirectView redirect = new RedirectView("/cuentas");
        cuentasContablesServicio.eliminarPorId(id);
        attributes.addFlashAttribute("exito", "Se ha eliminado correctamente la cuenta");
        return redirect;
    }


}
