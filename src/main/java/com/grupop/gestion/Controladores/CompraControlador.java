package com.grupop.gestion.Controladores;

import com.grupop.gestion.Entidades.Compra;
import com.grupop.gestion.Entidades.CompraDetalleImputacion;
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
@RequestMapping("/compras")
public class CompraControlador {

    private final CompraServicio compraServicio;
    private final EntidadBaseServicio entidadBaseServicio;
    private final TalonarioServicio talonarioServicio;
    private final TipoComprobanteServicio tipoComprobanteServicio;
    private final SectorServicio sectorServicio;
    private final TipoIvaServicio tipoIvaServicio;
    private final ProductoServicio productoServicio;
    private final CompraDetalleServicio compraDetalleServicio;
    private final FormaDePagoServicio formaDePagoServicio;
    private final CuentasContablesServicio cuentasContablesServicio;
    private final CompraDetalleImputacionServicio compraDetalleImputacionServicio;


    @GetMapping
    public ModelAndView getAll(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("tabla-compras");
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if(inputFlashMap!=null){ mav.addObject("exito", inputFlashMap.get("exito")); }
        mav.addObject("listaCompras", compraServicio.obtenerTodas());
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
        mav.addObject("listaProveedores", entidadBaseServicio.obtenerProveedores());
        mav.addObject("listaTalonario", talonarioServicio.obtenerTodos());
        mav.addObject("listaTipoComp", tipoComprobanteServicio.obtenerTodos());
        mav.addObject("listaSector", sectorServicio.obtenerTodos());
        mav.addObject("listaProd", productoServicio.obtenerTodos());
        mav.addObject("listaFormasPago", formaDePagoServicio.obtenerTodos());
        return mav;
    }

    @GetMapping("/form/{id}")
    public ModelAndView getFormUpd(@PathVariable Long id){
        ModelAndView mav = new ModelAndView("form-compras");
        //agregar validacion para no modificar en caso de que posea algun pago
        mav.addObject("compra", compraServicio.obtenerPorId(id));
        mav.addObject("action", "update");
        mav.addObject("listaProveedores", entidadBaseServicio.obtenerProveedores());
        mav.addObject("listaTalonario", talonarioServicio.obtenerTodos());
        mav.addObject("listaTipoComp", tipoComprobanteServicio.obtenerTodos());
        mav.addObject("listaSector", sectorServicio.obtenerTodos());
        mav.addObject("listaProd", productoServicio.obtenerActivos(true));
        mav.addObject("listaDetalle", compraDetalleServicio.obtenerPorCompra(id));
        mav.addObject("listaFormasPago", formaDePagoServicio.obtenerTodosPorOperacion(2l));
        mav.addObject("tablaDetalleImputacion", compraDetalleImputacionServicio.obtenerPorCompra(id));
        mav.addObject("listaCuentasImp", cuentasContablesServicio.obtenerTodos());

        return mav;
    }


    @PostMapping("/create")
    public RedirectView create (Compra dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/compras");
        try{
            compraServicio.crear(dto);
            redirect.setUrl("/compras/form/" + compraServicio.buscarUltimoId());
            attributes.addFlashAttribute("exito", "Se ha generado la compra correctamente");
            attributes.addFlashAttribute("compra", dto);
        }catch (Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            attributes.addFlashAttribute("compra", dto);
            redirect.setUrl("/compras/form");
        }
        return redirect;
    }

    @PostMapping("/update")
    public RedirectView update(Compra dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/compras");
        try{
            compraServicio.actualizar(dto);
            redirect.setUrl("/compras/form/" + dto.getId());
            attributes.addFlashAttribute("exito", "Se ha actualizado correctamente el registro");
        } catch (Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            attributes.addFlashAttribute("venta", dto);
            redirect.setUrl("/compras/form");
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
            System.out.println("Id CPA: " + idCompra + " Total: " + total);

            Double totalDouble = Double.valueOf(total);
            compraServicio.actualizarTotal(idCompra, totalDouble);
            attributes.addFlashAttribute("exito", "Se guardaron los cambios de detalle correctamente");
        } catch (Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            System.out.println(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Registro actualizado correctamente");

    }





}










