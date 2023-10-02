package com.grupop.gestion.Controladores;

import com.grupop.gestion.Entidades.Pago;
import com.grupop.gestion.Entidades.Proveedor;
import com.grupop.gestion.Servicios.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
@RequestMapping("/pago")
public class PagoControlador {

    private final PagoServicio pagoServicio;
    private final TalonarioServicio talonarioServicio;
    private final TipoComprobanteServicio tipoComprobanteServicio;
    private final SectorServicio sectorServicio;
    private final FormaDePagoServicio formaDePagoServicio;
    private final CompraServicio compraServicio;
    private final PagoDetalleServicioCtaCte pagoDetalleServicioCtaCte;
    private final CuentasContablesServicio cuentasContablesServicio;
    private final PagoDetalleImputacionServicio pagoDetalleImputacionServicio;
    private final FormaDePagoDetalleServicio formaDePagoDetalleServicio;
    private final ProveedorServicio proveedorServicio;
    private final PagoDetalleAdelantoServicio pagoDetalleAdelantoServicio;

    @GetMapping
    public ModelAndView getAll(@RequestParam Map<String, Object> params,  HttpServletRequest request){
        ModelAndView mav = new ModelAndView("tabla-pagos");
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if(inputFlashMap != null) mav.addObject("exito", inputFlashMap.get("exito"));

        int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;
        Page<Pago> pagePagos = pagoServicio.obtenerTodos(page, 50);
        int totalPage = pagePagos.getTotalPages();
        if(totalPage > 0){
            List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
            mav.addObject("pages", pages);
        }

        mav.addObject("listaPagos", pagePagos.getContent());
        mav.addObject("current", page + 1);
        mav.addObject("next", page + 2);
        mav.addObject("prev", page);
        mav.addObject("last", totalPage);
        mav.addObject("object", "pago");
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
        mav.addObject("listaProveedores", proveedorServicio.obtenerTodos());
        mav.addObject("listaTalonario", talonarioServicio.obtenerTodos());
        mav.addObject("listaTipoComp", tipoComprobanteServicio.obtenerTodos());
        mav.addObject("listaSector", sectorServicio.obtenerTodos());
        mav.addObject("listaFormasPago", formaDePagoServicio.obtenerTodosPorOperacion(4l));
        return mav;
    }

    @GetMapping("/form/{id}")
    public ModelAndView getFormUpd(@PathVariable Long id, HttpServletRequest request){
        ModelAndView mav = new ModelAndView("form-pagos");
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        Pago p = pagoServicio.obtenerPorId(id);
        mav.addObject("action", "update");
        mav.addObject("pago", p);
        mav.addObject("listaProveedores", proveedorServicio.obtenerTodos());
        mav.addObject("listaTalonario", talonarioServicio.obtenerTodos());
        mav.addObject("listaTipoComp", tipoComprobanteServicio.obtenerTodos());
        mav.addObject("listaSector", sectorServicio.obtenerTodos());
        mav.addObject("listaFormasPago", formaDePagoServicio.obtenerTodosPorOperacion(4l));
        mav.addObject("listaComprasPendientesPago", compraServicio.obtenerComprasPendientesPagoPorProveedor(p.getProveedor().getId()));
        mav.addObject("tablaDetalleCtaCte", pagoDetalleServicioCtaCte.obtenerPorPagoId(p.getId()));
        mav.addObject("listaCuentasImp", cuentasContablesServicio.obtenerTodos());
        mav.addObject("tablaDetalleImp", pagoDetalleImputacionServicio.obtenerPorPago(p.getId()));
        mav.addObject("fechaComprobante", p.getFechaComprobante());
        mav.addObject("listaAdelanto", pagoDetalleAdelantoServicio.obtenerPorPago(id));
        if(inputFlashMap != null) mav.addObject("exception" , inputFlashMap.get("exception"));
        return mav;
    }

    @PostMapping("/create")
    public RedirectView create (HttpServletRequest request, Pago dto, RedirectAttributes attributes){
        RedirectView r = new RedirectView("/pago");
        try{
            String fechaComprobante = request.getParameter("fechaAlta");
            pagoServicio.crear(dto, fechaComprobante);
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
    public RedirectView update(HttpServletRequest request,  Pago dto, RedirectAttributes attributes){
        RedirectView r = new RedirectView("/pago");
        try{
            String fechaComprobante = request.getParameter("fechaAlta");
            pagoServicio.actualizar(dto, fechaComprobante);
            Pago p = pagoServicio.obtenerPorId(dto.getId());
            r.setUrl("/pago/form/" + dto.getId());
            attributes.addFlashAttribute("exito", "Se ha actualizado correctamente el pago");
            if(dto.getFormaDePago() != null) {
                if ((dto.getFormaDePago().getId() == 54 || p.getFormaDePago().getId() == 54) && formaDePagoDetalleServicio.validarExistenciaSubDetalle(p.getId(), 4l) == 0) {
                    r.setUrl("/detalleDePago/getForm/" + dto.getId() + "/" + "4");
                }
            }

        }catch (Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            r.setUrl("/pago/form/" + dto.getId());
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
