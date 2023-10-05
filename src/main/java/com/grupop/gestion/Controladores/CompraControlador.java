package com.grupop.gestion.Controladores;

import com.grupop.gestion.Entidades.Compra;
import com.grupop.gestion.Entidades.CompraDetalleImputacion;
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
@RequestMapping("/compras")
public class CompraControlador {

    private final CompraServicio compraServicio;
    private final TalonarioServicio talonarioServicio;
    private final TipoComprobanteServicio tipoComprobanteServicio;
    private final SectorServicio sectorServicio;
    private final ProductoServicio productoServicio;
    private final CompraDetalleServicio compraDetalleServicio;
    private final FormaDePagoServicio formaDePagoServicio;
    private final CuentasContablesServicio cuentasContablesServicio;
    private final CompraDetalleImputacionServicio compraDetalleImputacionServicio;
    private final FormaDePagoDetalleServicio formaDePagoDetalleServicio;
    private final ProveedorServicio proveedorServicio;


    @GetMapping
    public ModelAndView getAll(@RequestParam Map<String, Object> params,  HttpServletRequest request){
        ModelAndView mav = new ModelAndView("tabla-compras");
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if(inputFlashMap!=null){ mav.addObject("exito", inputFlashMap.get("exito")); }

        int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;
        Page<Compra> pageCompras = compraServicio.obtenerTodas(page, 50);
        int totalPage = pageCompras.getTotalPages();
        if(totalPage > 0){
            List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
            mav.addObject("pages", pages);
        }
        mav.addObject("listaCompras", pageCompras.getContent());
        mav.addObject("current", page + 1);
        mav.addObject("next", page + 2);
        mav.addObject("prev", page);
        mav.addObject("last", totalPage);
        mav.addObject("object", "compras");
        return mav;
    }

    @GetMapping("/form")
    public ModelAndView getForm(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("form-compras");
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if (inputFlashMap != null) {
            mav.addObject("exception", inputFlashMap.get("exception"));
            mav.addObject("compra", inputFlashMap.get("compra"));
        } else {
            mav.addObject("compra", new Compra());
        }
        mav.addObject("action", "create");
        mav.addObject("listaProveedores", proveedorServicio.obtenerTodos());
        mav.addObject("listaTalonario", talonarioServicio.obtenerTodos());
        mav.addObject("listaTipoComp", tipoComprobanteServicio.obtenerTodos());
        mav.addObject("listaSector", sectorServicio.obtenerTodos());
        mav.addObject("listaProd", productoServicio.obtenerTodos());
        mav.addObject("listaFormasPago", formaDePagoServicio.obtenerTodosPorOperacion(2l));
        return mav;
    }

    @GetMapping("/form/{id}")
    public ModelAndView getFormUpd(@PathVariable Long id, HttpServletRequest request){
        ModelAndView mav = new ModelAndView("form-compras");
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        Compra c = compraServicio.obtenerPorId(id);
        mav.addObject("compra", c);
        mav.addObject("action", "update");
        mav.addObject("listaProveedores", proveedorServicio.obtenerTodos());
        mav.addObject("listaTalonario", talonarioServicio.obtenerTodos());
        mav.addObject("listaTipoComp", tipoComprobanteServicio.obtenerTodos());
        mav.addObject("listaSector", sectorServicio.obtenerTodos());
        mav.addObject("listaProd", productoServicio.obtenerActivosCompra(true));
        mav.addObject("listaDetalle", compraDetalleServicio.obtenerPorCompra(id));
        mav.addObject("listaFormasPago", formaDePagoServicio.obtenerTodosPorOperacion(2l));
        mav.addObject("tablaDetalleImputacion", compraDetalleImputacionServicio.obtenerPorCompra(id));
        mav.addObject("listaCuentasImp", cuentasContablesServicio.obtenerTodos());
        mav.addObject("fechaComprobante", c.getFechaComprobante());
        if(inputFlashMap != null) mav.addObject("exception", inputFlashMap.get("exception"));
        return mav;
    }


    @PostMapping("/create")
    public RedirectView create (@RequestParam String fechaAlta, @RequestParam(required = false) MultipartFile photo, Compra dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/compras");
        try{
            compraServicio.crear(dto, fechaAlta, photo);
            redirect.setUrl("/compras/form/" + compraServicio.buscarUltimoId());
            attributes.addFlashAttribute("exito", "Se ha generado la compra correctamente");
            attributes.addFlashAttribute("compra", dto);
        }catch (Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            attributes.addFlashAttribute("compra", dto);
            e.printStackTrace();
            redirect.setUrl("/compras/form");
        }
        return redirect;
    }

    @PostMapping("/update")
    public RedirectView update(@RequestParam String fechaAlta, @RequestParam(required = false) MultipartFile photo, Compra dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/compras/form/" + dto.getId());
        try{
            compraServicio.actualizar(dto, fechaAlta, photo);
            Compra c = compraServicio.obtenerPorId(dto.getId());

            if(!c.isBloqueado()){
                if(dto.getFormaDePago() != null || c.getFormaDePago() != null){
                    if( (dto.getFormaDePago().getId() == 60 || c.getFormaDePago().getId() == 60) && formaDePagoDetalleServicio.validarExistenciaSubDetalle(c.getId(), 2l) == 0){
                        redirect.setUrl("/detalleDePago/getForm/" + dto.getId() + "/" + "2");
                    }
                }
            }

            attributes.addFlashAttribute("exito", "Se ha actualizado correctamente el registro");
        } catch (Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            e.getMessage();
            redirect.setUrl("/compras/form/" + dto.getId());
        }
    return redirect;
    }

    @GetMapping("/delete/{id}")
    public RedirectView delete(@PathVariable Long id, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/compras");
        compraServicio.eliminarPorId(id);
        attributes.addFlashAttribute("exito", "Se ha eliminado correctamente el registro");
        return redirect;
    }

    @GetMapping("/obtenerTotalPorId/{id}")
    public ResponseEntity<Double> obtenerTotalPorId(@PathVariable Long id){
        return ResponseEntity.ok(compraServicio.obtenerTotalPorId(id));
    }

    @PostMapping("/actualizarTotalCompra/{idCompra}/{total}")
    public ResponseEntity<String> actualizarTotal(@PathVariable Long idCompra, @PathVariable String total, RedirectAttributes attributes){
        try {
            Double totalDouble = Double.valueOf(total);
            compraServicio.actualizarTotal(idCompra, new BigDecimal(totalDouble));
            attributes.addFlashAttribute("exito", "Se guardaron los cambios de detalle correctamente");
        } catch (Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            System.out.println(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Registro actualizado correctamente");

    }

    @PostMapping("/actualizarTotalCompra/{idCompra}")
    public ResponseEntity<String> actualizarTotal(@PathVariable Long idCompra, RedirectAttributes attributes){
        try{
            compraServicio.actualizarTotal(idCompra);
        } catch (Exception e){
            System.out.println("Excepcion");
            attributes.addFlashAttribute("exception", e.getMessage());
            System.out.println(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Se actualizo el total correctamente");
    }

    @GetMapping("/validarEstado/{idCompra}")
    public ResponseEntity<Boolean> validarEstado(@PathVariable Long idCompra){
        return  ResponseEntity.ok(compraServicio.validarEstado(idCompra));
    }





}










