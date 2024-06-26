package com.grupop.gestion.Controladores;

import com.grupop.gestion.DTO.CreditoDetalleDto;
import com.grupop.gestion.Entidades.*;
import com.grupop.gestion.Servicios.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    private final FormaDePagoDetalleSubDetalleServicio formaDePagoDetalleSubDetalleServicio;
    private final EstadoCreditoServicio estadoCreditoServicio;
    private final EmailService emailService;


    @GetMapping
    public ModelAndView getAll(@RequestParam Map<String, Object> params ,  HttpServletRequest request){
        ModelAndView mav = new ModelAndView("tabla-credito");
        Map<String,?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
            if(inputFlashMap!=null) mav.addObject("exito", inputFlashMap.get("exito"));

            int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) -1 ) : 0;
            Page<Credito> pageCreditos = creditoServicio.obtenerTodos(page, 50);
            int totalPage = pageCreditos.getTotalPages();
            if(totalPage > 0){
                List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
                mav.addObject("pages", pages);
            }
            mav.addObject("listaCreditos", pageCreditos.getContent());
            mav.addObject("current", page + 1);
            mav.addObject("next", page + 2);
            mav.addObject("prev", page);
            mav.addObject("last", totalPage);
            mav.addObject("object", "credito");
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
        mav.addObject("listaEstados", estadoCreditoServicio.obtenerTodos());
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
        mav.addObject("listaEstados", estadoCreditoServicio.obtenerTodos());
        return mav;
    }


    @GetMapping("/form/new/{idVenta}")
    public ModelAndView getFormCharged(HttpServletRequest request, @PathVariable Long idVenta){
        ModelAndView mav = new ModelAndView("form-credito");
        Map<String,?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        Venta v = ventaServicio.obtenerPorId(idVenta);

        if(inputFlashMap!=null){
            mav.addObject("exception", inputFlashMap.get("exception"));
            mav.addObject("credito", inputFlashMap.get("credito"));
        }else{
            mav.addObject("credito", new Credito());
        }
        mav.addObject("action", "create");
        mav.addObject("listaPlanPago", planPagoServicio.obtenerTodos());
        mav.addObject("venta", v);
        mav.addObject("listaSector", sectorServicio.obtenerTodos());
        mav.addObject("listaTipoCompro", tipoComprobanteServicio.obtenerTodos());
        mav.addObject("listaTalonario", talonarioServicio.obtenerTodos());
        mav.addObject("detallePago", formaDePagoDetalleSubDetalleServicio.obtenerPorIdOperacionAndIdTipoOperacion(v.getId(), v.getTipoOperacion().getId()));
        mav.addObject("listaEstados", estadoCreditoServicio.obtenerTodos());
        return mav;
    }

    @GetMapping("/form/new/{idVenta}/{importe}")
    public ModelAndView getFormCharged(HttpServletRequest request, @PathVariable Long idVenta, @PathVariable BigDecimal importe){
        ModelAndView mav = new ModelAndView("form-credito");
        Map<String,?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);

        if(inputFlashMap!=null){
            System.out.println(inputFlashMap);
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
        mav.addObject("listaEstados", estadoCreditoServicio.obtenerTodos());
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
        RedirectView r = new RedirectView("/credito/form/" + dto.getId());
        try{
            creditoServicio.actualizar(dto);
            Credito credito = creditoServicio.obtenerPorId(dto.getId());
            //validar que no exista un nuevo credito que haya refinanciado
            if(credito.getEstadoCredito().getId() == 3l && !creditoServicio.verificarSiHayUnCreditoActivoPorFkVenta(credito.getVenta().getId())){
                System.out.println("Refinanciar credito");
                r.setUrl("/credito/form/new/" + credito.getVenta().getId() + "/" + creditoServicio.obtenerImporteRefinancia(credito.getId()));
                attributes.addFlashAttribute("credito", new Credito());
            }

            attributes.addFlashAttribute("exito", "Se ha actualizado el credito correctamente");
        } catch (Exception e){
            e.printStackTrace();
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
    @GetMapping("/exportarPDF/{idCredito}")
    public ResponseEntity<byte[]> generarReporteCredito(HttpServletResponse response, @PathVariable Long idCredito){
        try{
            return creditoServicio.exportInvoice(idCredito);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/obtenerCuotasCobrarAtrasados")
    public ModelAndView obtenerCuotasCobrarAtrasados(){
        ModelAndView mav = new ModelAndView("tabla-cuotasCobrarMensual");
        mav.addObject("listaCuotas", creditoServicio.obtenerCuotasCobrarAtrasados());
        mav.addObject("titulo", "Atrasadas");
        return mav;
    }

    @GetMapping("/enviarCorreo/{idCreditoDetalle}/{fechaVencimiento}/{monto}/{titulo}")
    public RedirectView enviarCorreoIndividual(@PathVariable Long idCreditoDetalle, @PathVariable LocalDate fechaVencimiento, @PathVariable BigDecimal monto, @PathVariable String titulo , RedirectAttributes attributes){
        RedirectView r;
        if(titulo.equals("Atrasadas")){
            r = new RedirectView("/credito/obtenerCuotasCobrarAtrasados");

            CreditoDetalle c = creditoDetalleServicio.obtenerPorId(idCreditoDetalle);
            EntidadBase entidadBase = entidadBaseServicio.obtenerNombrePorFkCliente(c.getCliente().getId());

            try{
                emailService.sendCobranza(entidadBase.getCorreo(), "atrasados",  entidadBase.getRazonSocial(), fechaVencimiento, monto, "Informacion de Cuotas Atrasadas");
                attributes.addFlashAttribute("exito", "Se envio el correo correctamente");
            }catch (Exception e){
                e.printStackTrace();
                attributes.addFlashAttribute("exception", "Error al enviar el correo");
            }

        }else{
            r = new RedirectView("/credito/obtenerCuotasCobrarMensual");

            CreditoDetalle c = creditoDetalleServicio.obtenerPorId(idCreditoDetalle);
            EntidadBase entidadBase = entidadBaseServicio.obtenerNombrePorFkCliente(c.getCliente().getId());

            try{
                emailService.sendCobranza(entidadBase.getCorreo(), "mensualidad",  entidadBase.getRazonSocial(), fechaVencimiento, monto, "Informacion de Cuota Mensual");
                attributes.addFlashAttribute("exito", "Se envio el correo correctamente");
            }catch (Exception e){
                e.printStackTrace();
                attributes.addFlashAttribute("exception", "Error al enviar el correo");
            }

        }


        return r;
    }

    @GetMapping("/obtenerCuotasCobrarMensual")
    public ModelAndView obtenerCuotasCobrarMensual(){
        ModelAndView mav = new ModelAndView("tabla-cuotasCobrarMensual");
        mav.addObject("listaCuotas", creditoServicio.obtenerCuotasCobrarMensual());
        mav.addObject("titulo", "Corrientes");
        return mav;
    }


    @GetMapping("/obtenerTelefonoCliente/{idCreditoDetalle}")
    public ResponseEntity<Long> obtenerTelefonoByIdCreditoDetalle(@PathVariable Long idCreditoDetalle){
        CreditoDetalle c = creditoDetalleServicio.obtenerPorId(idCreditoDetalle);
        EntidadBase e = entidadBaseServicio.obtenerNombrePorFkCliente(c.getCliente().getId());
        return ResponseEntity.ok(e.getTelefono());
    }





}
