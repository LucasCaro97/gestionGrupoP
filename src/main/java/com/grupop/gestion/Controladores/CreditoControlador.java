package com.grupop.gestion.Controladores;

import com.grupop.gestion.DTO.CreditoDetalleDto;
import com.grupop.gestion.Entidades.Credito;
import com.grupop.gestion.Entidades.CreditoDetalle;
import com.grupop.gestion.Entidades.TipoComprobante;
import com.grupop.gestion.Servicios.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/credito")
public class CreditoControlador {

    private final CreditoServicio creditoServicio;
    private final PlanPagoServicio planPagoServicio;
    private final EntidadBaseServicio entidadBaseServicio;
    private final SectorServicio sectorServicio;
    private final TipoComprobanteServicio tipoComprobanteServicio;
    private final TalonarioServicio talonarioServicio;
    private final VentaServicio ventaServicio;
    private final CreditoDetalleServicio creditoDetalleServicio;


    @GetMapping
    public ModelAndView getAll(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("tabla-credito");
        Map<String,?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
            if(inputFlashMap!=null) mav.addObject("exito", inputFlashMap.get("exito"));
            mav.addObject("listaCreditos", creditoServicio.obtenerTodos());
            return mav;
    }

    @GetMapping("/form")
    public ModelAndView getForm(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("form-credito");
        Map<String,?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
            if(inputFlashMap!=null){
                mav.addObject("exception", inputFlashMap.get("exception"));
                mav.addObject("credito", inputFlashMap.get("credito"));
            }else{
                mav.addObject("credito", new Credito());
            }
        mav.addObject("action", "create");
        mav.addObject("procedencia", "nuevo");
        mav.addObject("listaPlanPago", planPagoServicio.obtenerTodos());
        mav.addObject("listaCliente", entidadBaseServicio.obtenerClientes());
        mav.addObject("listaSector", sectorServicio.obtenerTodos());
        mav.addObject("listaTipoCompro", tipoComprobanteServicio.obtenerTodos());
        mav.addObject("listaTalonario", talonarioServicio.obtenerTodos());
        return mav;
    }

    @GetMapping("/form/{idCredito}")
    public ModelAndView getFormVisual(HttpServletRequest request, @PathVariable Long idCredito){
        ModelAndView mav = new ModelAndView("form-credito");
        Credito cred = creditoServicio.obtenerPorId(idCredito);
        mav.addObject("credito", cred);
        mav.addObject("action", "update");
        mav.addObject("venta", ventaServicio.obtenerPorId(cred.getVenta().getId()));
        mav.addObject("listaPlanPago", planPagoServicio.obtenerTodos());
        mav.addObject("listaCliente", entidadBaseServicio.obtenerClientes());
        mav.addObject("listaSector", sectorServicio.obtenerTodos());
        mav.addObject("listaTipoCompro", tipoComprobanteServicio.obtenerTodos());
        mav.addObject("listaTalonario", talonarioServicio.obtenerTodos());
        mav.addObject("listaCreditoDetalle", creditoDetalleServicio.obtenerLineasDetalle(cred.getId()));
        return mav;
    }


    @GetMapping("/form/new/{idVenta}")
    public ModelAndView getFormCharged(HttpServletRequest request, @PathVariable Long idVenta){
        ModelAndView mav = new ModelAndView("form-credito");
        Map<String,?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);

        if(inputFlashMap!=null){
            mav.addObject("exception", inputFlashMap.get("exception"));
            mav.addObject("credito", inputFlashMap.get("credito"));
        }else{
            mav.addObject("credito", new Credito());
        }
        mav.addObject("action", "create");
        mav.addObject("listaPlanPago", planPagoServicio.obtenerTodos());
        mav.addObject("venta", ventaServicio.obtenerPorId(idVenta));
        mav.addObject("listaSector", sectorServicio.obtenerTodos());
        mav.addObject("listaTipoCompro", tipoComprobanteServicio.obtenerTodos());
        mav.addObject("listaTalonario", talonarioServicio.obtenerTodos());
        return mav;
    }

    @GetMapping("/form/new/{idVenta}/{importe}")
    public ModelAndView getFormCharged(HttpServletRequest request, @PathVariable Long idVenta, @PathVariable BigDecimal importe){
        ModelAndView mav = new ModelAndView("form-credito");
        Map<String,?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);

        if(inputFlashMap!=null){
            mav.addObject("exception", inputFlashMap.get("exception"));
            mav.addObject("credito", inputFlashMap.get("credito"));
        }else{
            mav.addObject("credito", new Credito());
        }
        mav.addObject("action", "create");
        mav.addObject("listaPlanPago", planPagoServicio.obtenerTodos());
        mav.addObject("venta", ventaServicio.obtenerPorId(idVenta));
        mav.addObject("listaSector", sectorServicio.obtenerTodos());
        mav.addObject("listaTipoCompro", tipoComprobanteServicio.obtenerTodos());
        mav.addObject("listaTalonario", talonarioServicio.obtenerTodos());
        mav.addObject("importe", importe);
        return mav;
    }

    @PostMapping("/create")
    public RedirectView create(Credito dto, RedirectAttributes attributes, HttpServletRequest request){

        RedirectView r = new RedirectView("/credito");
        String venceLosDias = request.getParameter("vencimiento");
        try{
            creditoServicio.crear(dto, venceLosDias);
            r.setUrl("/credito/form/" + creditoServicio.buscarUltimoId());
            attributes.addFlashAttribute("exito", "Se ha creado el credito correctamente");
        } catch (Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            attributes.addFlashAttribute("credito", dto);
            r.setUrl("/credito/form");
            System.out.println(e.getMessage());
        }
        return r;
    }

    @PostMapping("/update")
    public RedirectView update(Credito dto, RedirectAttributes attributes){
        RedirectView r = new RedirectView("/credito");
        try{
            creditoServicio.actualizar(dto);
            r.setUrl("/credito/form/" + dto.getId());
            attributes.addFlashAttribute("exito", "Se ha actualizado el credito correctamente");
        } catch (Exception e){
            System.out.println(e.getMessage());
            attributes.addFlashAttribute("exception", e.getMessage());
            attributes.addFlashAttribute("credito", dto);
            r.setUrl("/credito/form/" + dto.getId());
        }
        return r;
    }

    @GetMapping("/delete/{id}")
    public RedirectView delete(@PathVariable Long id, RedirectAttributes attributes){
        RedirectView r = new RedirectView("/credito");
        creditoServicio.eliminarPorId(id);
        attributes.addFlashAttribute("Se ha eliminado el registro correctamente");
        return r;
    }

    @GetMapping("/obtenerPorId/{id}")
    public ResponseEntity<Credito> obtenerPorId(@PathVariable Long id){
        return ResponseEntity.ok(creditoServicio.obtenerPorId(id));
    }

    @PostMapping("/regenerarCuotas/{idCredito}")
    public ResponseEntity<String> regenerarCuotas(@PathVariable Long idCredito, @RequestBody List<CreditoDetalleDto> arrayListCuotas){

        creditoServicio.regenerarCuotas(idCredito, arrayListCuotas);
        return ResponseEntity.status(HttpStatus.CREATED).body("Se ha regenerado el credito correctamente");
    }

    @GetMapping("/validarEstado/{idCredito}")
    public ResponseEntity<Boolean> validarEstado(@PathVariable Long idCredito){
        return  ResponseEntity.ok(creditoServicio.validarEstado(idCredito));
    }

    @GetMapping("/validarExistencia/{idOperacion}")
    public ResponseEntity<Integer> validarExistencia(@PathVariable Long idOperacion){
        return ResponseEntity.ok(creditoServicio.validarExistenciaPorVenta(idOperacion));
    }





}
