package com.grupop.gestion.Controladores;

import com.grupop.gestion.Entidades.Cheque;
import com.grupop.gestion.Entidades.Cobro;
import com.grupop.gestion.Entidades.FormaDePago;
import com.grupop.gestion.Entidades.TipoIva;
import com.grupop.gestion.Servicios.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    private final VentaServicio ventaServicio;
    private final CobroDetalleCtaCteServicio cobroDetalleCtaCteServicio;
    private final FormaDePagoDetalleServicio formaDePagoDetalleServicio;
    private final ChequeServicio chequeServicio;
    private final CobroDetalleAdelantoServicio cobroDetalleAdelantoServicio;

    @GetMapping
    public ModelAndView getAll(@RequestParam Map<String, Object> params,  HttpServletRequest request){
        ModelAndView mav = new ModelAndView("tabla-cobros");
        Map<String,?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);

        if(inputFlashMap!=null) mav.addObject("exito", inputFlashMap.get("exito"));

        int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0 ;
        Page<Cobro> pageCobros = cobroServicio.obtenerTodos(page, 50);
        int totalPage = pageCobros.getTotalPages();
        if(totalPage > 0){
            List<Integer> pages = IntStream.rangeClosed(1 , totalPage).boxed().collect(Collectors.toList());
            mav.addObject("pages", pages);
        }

        mav.addObject("listaCobros", pageCobros.getContent());
        mav.addObject("current", page + 1);
        mav.addObject("next", page + 2);
        mav.addObject("prev", page);
        mav.addObject("last", totalPage);
        mav.addObject("object", "cobros");
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
        mav.addObject("listaVentasCtaCte", ventaServicio.obtenerVtasCtaCtePorCliente(cobro.getCliente().getId()));
        mav.addObject("tablaDetalleCtaCte", cobroDetalleCtaCteServicio.obtenerTodosPorCobro(id));
        mav.addObject("fechaComprobante", cobro.getFechaComprobante());
        mav.addObject("listaAdelanto", cobroDetalleAdelantoServicio.obtenerPorCobro(id));
        return mav;
    }

    @PostMapping("/create")
    public RedirectView create(@RequestParam String fechaAlta, @RequestParam(required = false)MultipartFile photo, Cobro dto, RedirectAttributes attributes){
        RedirectView r = new RedirectView("/cobros");
        try{
            System.out.println(photo.getName());
            cobroServicio.crear(dto, fechaAlta, photo);
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
    public RedirectView update (@RequestParam String fechaAlta, @RequestParam(required = false)MultipartFile photo, Cobro dto, RedirectAttributes attributes){
        RedirectView r = new RedirectView("/cobros/form/" + dto.getId());
        try {
            System.out.println(photo.getName());
            cobroServicio.actualizar(dto, fechaAlta, photo);
            Cobro c = cobroServicio.obtenerPorId(dto.getId());

            if(dto.getFormaDePago() != null) {
                if ((dto.getFormaDePago().getId() == 53 || c.getFormaDePago().getId() == 53) && formaDePagoDetalleServicio.validarExistenciaSubDetalle(c.getId(), 3l) == 0) {
                    r.setUrl("/detalleDePago/getForm/" + dto.getId() + "/" + "3");
                }else if( ( dto.getFormaDePago().getId() == 15 || c.getFormaDePago().getId() == 15 ) && chequeServicio.validarExistencia(c.getId()) == 0 ){
                    r.setUrl("/cheque/form");
                }
            }else{
                attributes.addFlashAttribute("exito", "Se ha actualizado correctamente el cobro");
            }

        }catch (Exception e){
            attributes.addFlashAttribute("cobro", dto);
            attributes.addFlashAttribute("exception", e.getMessage());
            e.printStackTrace();
            r.setUrl("/cobros/form");
        }
        return r;
    }


    @GetMapping("delete/{id}")
    public RedirectView delete(@PathVariable Long id, RedirectAttributes attributes){
        RedirectView r = new RedirectView("/cobros");
        cobroServicio.eliminarPorId(id);
        attributes.addFlashAttribute("exito", "Se ha eliminado el cobro correctamente");
        return  r;
    }

    @PostMapping("/actualizarTotal/{idCobro}")
    public ResponseEntity<String> actualizarTotal(@PathVariable Long idCobro){
        try{
            cobroServicio.actualizarTotal(idCobro);
        }catch (Exception e){
            System.out.println("Exception");
            System.out.println(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("Se actualizo el total correctamente");
    }

    @GetMapping("/obtenerTotalPorId/{id}")
    public ResponseEntity<BigDecimal> obtenerTotalPorId(@PathVariable Long id){
        return ResponseEntity.ok(cobroServicio.obtenerTotalPorId(id));
    }


}
