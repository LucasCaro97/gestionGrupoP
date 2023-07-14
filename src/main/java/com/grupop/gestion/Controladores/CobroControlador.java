package com.grupop.gestion.Controladores;

import com.grupop.gestion.Entidades.Cobro;
import com.grupop.gestion.Entidades.FormaDePago;
import com.grupop.gestion.Entidades.TipoIva;
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
@RequestMapping("cobros")
public class CobroControlador {


    private final CobroServicio cobroServicio;
    private final ClienteServicio clienteServicio;
    private final TalonarioServicio talonarioServicio;
    private final SectorServicio sectorServicio;
    private final TipoComprobanteServicio tipoComprobanteServicio;
    private final MonedaServicio monedaServicio;
    private final FormaDePagoServicio formaDePagoServicio;
    private final TipoIvaServicio tipoIvaServicio;
    private final CreditoDetalleServicio creditoDetalleServicio;
    private final CobroDetalleCuotasServicio cobroDetalleCuotasServicio;


    @GetMapping
    public ModelAndView getAll(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("tabla-cobros");
        Map<String,?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);

        if(inputFlashMap!=null) mav.addObject("exito", inputFlashMap.get("exito"));
        mav.addObject("listaCobros", cobroServicio.obtenerTodos());
        return mav;
    }

    @GetMapping("/form")
    public ModelAndView getForm(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("form-cobros");
        Map<String , ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if(inputFlashMap!=null){
            mav.addObject("exception", inputFlashMap.get("exception"));
            mav.addObject("cobro", inputFlashMap.get("cobro"));
        }else {
            mav.addObject("cobro", new Cobro());
        }
        mav.addObject("action", "create");
        mav.addObject("listaClientes", clienteServicio.obtenerTodos());
        mav.addObject("listaTalonario", talonarioServicio.obtenerTodos());
        mav.addObject("listaTipoComp", tipoComprobanteServicio.obtenerTodos());
        mav.addObject("listaSector", sectorServicio.obtenerTodos());
        mav.addObject("listaMoneda", monedaServicio.obtenerTodos());
        mav.addObject("listaFormasPago", formaDePagoServicio.obtenerTodosPorOperacion(3l));
        mav.addObject("listaIva", tipoIvaServicio.obtenerTodos());
        return mav;
    }

    @GetMapping("/form/{id}")
    public ModelAndView getFormUpd(@PathVariable Long id){
        ModelAndView mav = new ModelAndView("form-cobros");
        Cobro cobro = cobroServicio.obtenerPorId(id);
        mav.addObject("cobro", cobro);
        mav.addObject("listaClientes", clienteServicio.obtenerTodos());
        mav.addObject("listaTalonario", talonarioServicio.obtenerTodos());
        mav.addObject("listaTipoComp", tipoComprobanteServicio.obtenerTodos());
        mav.addObject("listaSector", sectorServicio.obtenerTodos());
        mav.addObject("action", "update");
        mav.addObject("listaMoneda", monedaServicio.obtenerTodos());
        mav.addObject("listaFormasPago", formaDePagoServicio.obtenerTodosPorOperacion(3l));
        mav.addObject("listaIva", tipoIvaServicio.obtenerTodos());
        mav.addObject("listaCuotasCliente", creditoDetalleServicio.obtenerCreditosPendientesPorFkCliente(cobro.getCliente().getId()));
        mav.addObject("tablaDetalleCuotas", cobroDetalleCuotasServicio.obtenerPorCobro(id));
        return mav;
    }

    @PostMapping("/create")
    public RedirectView create(Cobro dto, RedirectAttributes attributes){
        RedirectView r = new RedirectView("/cobros");
        try{
            cobroServicio.crear(dto);
            r.setUrl("/cobros/form/" + ( cobroServicio.buscarUltimoId()));
            attributes.addFlashAttribute("exito", "Se ha registrado el cobro correctamente");
        }catch (Exception e){
            attributes.addFlashAttribute("cobro", dto);
            attributes.addFlashAttribute("exception", e.getMessage());
            System.out.println(e.getMessage());
            r.setUrl("/cobros/form");
        }
        return r;
    }

    @PostMapping("/update")
    public RedirectView update (Cobro dto, RedirectAttributes attributes){
        RedirectView r = new RedirectView("/cobros/form/" + dto.getId());
        try {
            cobroServicio.actualizar(dto);
            attributes.addFlashAttribute("exito", "Se ha actualizado correctamente el cobro");
        }catch (Exception e){
            attributes.addFlashAttribute("cobro", dto);
            attributes.addFlashAttribute("exception", e.getMessage());
            System.out.println(e.getMessage());
            r.setUrl("/cobros/form");
        }
        return r;
    }


    @GetMapping("delete/{id}")
    public RedirectView delete(Long id, RedirectAttributes attributes){
        RedirectView r = new RedirectView("/cobros");
        cobroServicio.eliminarPorId(id);
        attributes.addFlashAttribute("exito", "Se ha eliminado el cobro correctamente");
        return  r;
    }

}
