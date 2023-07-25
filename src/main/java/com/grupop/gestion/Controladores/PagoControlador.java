package com.grupop.gestion.Controladores;

import com.grupop.gestion.Entidades.Pago;
import com.grupop.gestion.Servicios.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/pago")
public class PagoControlador {

    private final PagoServicio pagoServicio;
    private final EntidadBaseServicio entidadBaseServicio;
    private final TalonarioServicio talonarioServicio;
    private final TipoComprobanteServicio tipoComprobanteServicio;
    private final SectorServicio sectorServicio;
    private final FormaDePagoServicio formaDePagoServicio;
    private final CompraServicio compraServicio;
    private final PagoDetalleServicioCtaCte pagoDetalleServicioCtaCte;
    private final CuentasContablesServicio cuentasContablesServicio;
    private final PagoDetalleImputacionServicio pagoDetalleImputacionServicio;


    @GetMapping
    public ModelAndView getAll(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("tabla-pagos");
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if(inputFlashMap != null) mav.addObject("exito", inputFlashMap.get("exito"));
        mav.addObject("listaPagos", pagoServicio.obtenerTodos());
        return mav;
    }

    @GetMapping("/form")
    public ModelAndView getForm(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("form-pagos");
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);

        if(inputFlashMap!=null){
            mav.addObject("exception", inputFlashMap.get("exception"));
            mav.addObject("pago", inputFlashMap.get("pago"));
        }else{
            mav.addObject("pago", new Pago());
        }

        mav.addObject("action", "create");
        mav.addObject("listaProveedores", entidadBaseServicio.obtenerProveedores());
        mav.addObject("listaTalonario", talonarioServicio.obtenerTodos());
        mav.addObject("listaTipoComp", tipoComprobanteServicio.obtenerTodos());
        mav.addObject("listaSector", sectorServicio.obtenerTodos());
        mav.addObject("listaFormasPago", formaDePagoServicio.obtenerTodosPorOperacion(4l));
        return mav;
    }

    @GetMapping("/form/{id}")
    public ModelAndView getFormUpd(@PathVariable Long id){
        ModelAndView mav = new ModelAndView("form-pagos");
        Pago p = pagoServicio.obtenerPorId(id);
        mav.addObject("action", "update");
        mav.addObject("pago", p);
        mav.addObject("listaProveedores", entidadBaseServicio.obtenerProveedores());
        mav.addObject("listaTalonario", talonarioServicio.obtenerTodos());
        mav.addObject("listaTipoComp", tipoComprobanteServicio.obtenerTodos());
        mav.addObject("listaSector", sectorServicio.obtenerTodos());
        mav.addObject("listaFormasPago", formaDePagoServicio.obtenerTodosPorOperacion(4l));
        mav.addObject("listaComprasPendientesPago", compraServicio.obtenerComprasPendientesPagoPorProveedor(p.getProveedor().getId()));
        mav.addObject("tablaDetalleCtaCte", pagoDetalleServicioCtaCte.obtenerPorPagoId(p.getId()));
        mav.addObject("listaCuentasImp", cuentasContablesServicio.obtenerTodos());
        mav.addObject("tablaDetalleImp", pagoDetalleImputacionServicio.obtenerPorPago(p.getId()));
        return mav;
    }

    @PostMapping("/create")
    public RedirectView create (Pago dto, RedirectAttributes attributes){
        RedirectView r = new RedirectView("/pago");
        try{
            pagoServicio.crear(dto);
            r.setUrl("/pago/form/" + pagoServicio.buscarUltimoId());
            attributes.addFlashAttribute("exito", "Se ha generado el pago correctamente");
            attributes.addFlashAttribute("pago", dto);
        }catch(Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            attributes.addFlashAttribute("pago", dto);
            r.setUrl("/pago/form");
        }
        return r;
    }

    @PostMapping("/update")
    public RedirectView update(Pago dto, RedirectAttributes attributes){
        RedirectView r = new RedirectView("/pago");
        try{
            pagoServicio.actualizar(dto);
            r.setUrl("/pago/form/" + dto.getId());
            attributes.addFlashAttribute("exito", "Se ha actualizado correctamente el pago");
        }catch (Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            attributes.addFlashAttribute("pago", dto);
            r.setUrl("/pago/form");
        }
        return r;
    }

    @GetMapping("/delete/{id}")
    public RedirectView delete(@PathVariable Long id, RedirectAttributes attributes){
        RedirectView r = new RedirectView("/pago");
        pagoServicio.eliminarPorId(id);
        attributes.addFlashAttribute("exito", "Se ha eliminado con exito el registro");
        return r;
    }

    @GetMapping("/obtenerTotalPorId/{id}")
    public ResponseEntity<Double> obtenerTotalPorId(@PathVariable Long id){
        return ResponseEntity.ok(pagoServicio.obtenerTotalPorId(id));
    }

    @PostMapping("/actualizarTotalPago/{idPago}")
    public ResponseEntity<String> actualizarTotal(@PathVariable Long idPago){
        try{
            pagoServicio.actualizarTotal(idPago);
        } catch (Exception e){
            System.out.println("Excepcion");
            System.out.println(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Se actualizo el total correctamente");
    }

}
