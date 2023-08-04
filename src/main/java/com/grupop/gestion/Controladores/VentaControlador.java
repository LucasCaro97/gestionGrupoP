package com.grupop.gestion.Controladores;


import com.grupop.gestion.Entidades.Credito;
import com.grupop.gestion.Entidades.Manzana;
import com.grupop.gestion.Entidades.Venta;
import com.grupop.gestion.Repositorios.ProductoRepo;
import com.grupop.gestion.Repositorios.VentaRepo;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ventas")
public class VentaControlador {

    private final VentaServicio ventaServicio;
    private final EntidadBaseServicio entidadBaseServicio;
    private final TalonarioServicio talonarioServicio;
    private final TipoComprobanteServicio tipoComprobanteServicio;
    private final SectorServicio sectorServicio;
    private final MonedaServicio monedaServicio;
    private final TipoIvaServicio tipoIvaServicio;
    private final ProductoServicio productoServicio;
    private final VentaDetalleServicio ventaDetalleServicio;
    private final FormaDePagoServicio formaDePagoServicio;
    private final CuentasContablesServicio cuentasContablesServicio;
    private final VentaDetalleImputacionServicio ventaDetalleImputacionServicio;
    private final CreditoServicio creditoServicio;
    private final ClienteServicio clienteServicio;
    private final ComisionServicio comisionServicio;
    private final IndiceCacServicio indiceCacServicio;
    private final FormaDePagoDetalleServicio formaDePagoDetalleServicio;


    @GetMapping
    public ModelAndView getAll (HttpServletRequest request){
        ModelAndView mav = new ModelAndView("tabla-ventas");
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if(inputFlashMap!=null){ mav.addObject("exito", inputFlashMap.get("exito")); }
        mav.addObject("listaVentas", ventaServicio.obtenerTodas());
        return mav;
    }

    @GetMapping("/form")
    public ModelAndView getForm(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("form-ventas");
        Map<String,?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if(inputFlashMap!=null) {
        mav.addObject("exception", inputFlashMap.get("exception"));
        mav.addObject("venta", inputFlashMap.get("venta"));
        }else {
            mav.addObject("venta", new Venta());
        }
        mav.addObject("action", "create");
        mav.addObject("listaClientes", clienteServicio.obtenerTodos());
        mav.addObject("listaTalonario", talonarioServicio.obtenerTodos() );
        mav.addObject("listaTipoComp", tipoComprobanteServicio.obtenerTodos());
        mav.addObject("listaSector", sectorServicio.obtenerTodos());
        mav.addObject("listaMoneda", monedaServicio.obtenerTodos());
        mav.addObject("listaIva", tipoIvaServicio.obtenerTodos());
        mav.addObject("listaProd", productoServicio.obtenerTodos());
        mav.addObject("listaFormasPago", formaDePagoServicio.obtenerTodosPorOperacion(1l));
        mav.addObject("listaVendedores", entidadBaseServicio.obtenerVendedores());
        mav.addObject("indiceBase", indiceCacServicio.obtenerTodos());
        return mav;
    }

    @GetMapping("/form/{id}")
    public ModelAndView getFormUpd(@PathVariable Long id){
        ModelAndView mav = new ModelAndView("form-ventas");
        Venta vta = ventaServicio.obtenerPorId(id);
        mav.addObject("venta",vta);
        mav.addObject("action", "update");
        mav.addObject("listaClientes", entidadBaseServicio.obtenerClientes());
        mav.addObject("listaTalonario", talonarioServicio.obtenerTodos() );
        mav.addObject("listaTipoComp", tipoComprobanteServicio.obtenerTodos());
        mav.addObject("listaSector", sectorServicio.obtenerTodos());
        mav.addObject("listaMoneda", monedaServicio.obtenerTodos());
        mav.addObject("listaIva", tipoIvaServicio.obtenerTodos());
        mav.addObject("listaProd", productoServicio.obtenerActivos(true));
        mav.addObject("listaDetalle", ventaDetalleServicio.obtenerPorVenta(id));
        mav.addObject("listaFormasPago", formaDePagoServicio.obtenerTodosPorOperacion(1l));
        mav.addObject("tablaDetalleImputacion", ventaDetalleImputacionServicio.obtenerPorVenta(id));
        mav.addObject("listaCuentasImp", cuentasContablesServicio.obtenerTodos());
        mav.addObject("listaVendedores", entidadBaseServicio.obtenerVendedores());
        mav.addObject("listaComisiones", comisionServicio.obtenerComisionVenta(id));
        mav.addObject("listaIndice", indiceCacServicio.obtenerTodos());
        return mav;
    }

    @PostMapping("/create")
    public RedirectView create(Venta dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/ventas");
        try{
            ventaServicio.crear(dto);
            redirect.setUrl("/ventas/form/" + ( ventaServicio.buscarUltimoId()));
            attributes.addFlashAttribute("exito", "Se ha generado el registro correctamente");
            attributes.addFlashAttribute("venta", dto);
        }catch (Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            attributes.addFlashAttribute("venta", dto);
            redirect.setUrl("/ventas/form");
        }
        return redirect;
    }

    @PostMapping("/update")
    public RedirectView update(Venta dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/ventas/form/" + dto.getId());

        try{
            ventaServicio.actualizar(dto);
            Venta v = ventaServicio.obtenerPorId(dto.getId());

            if(!v.isBloqueada()){
                if(dto.getFormaDePago() != null || v.getFormaDePago() != null ){
                   if( (dto.getFormaDePago().getId() == 3 || v.getFormaDePago().getId() == 3 ) && creditoServicio.validarExistenciaPorVenta(dto.getId()) == 0){
                       redirect.setUrl("/credito/form/new/"+dto.getId());
                   }else if( (dto.getFormaDePago().getId() == 51 || v.getFormaDePago().getId() == 51 ) && formaDePagoDetalleServicio.validarExistenciaSubDetalle(v.getId(), 1l) == 0 ){   //Validar tambien luego si existe ya un detalle de pago asignado
                       redirect.setUrl("/detalleDePago/getForm/"+dto.getId() + "/" + "1");
                   }
                }
            }else{
                System.out.println("La venta esta bloqueada, no es necesario redirigir a credito");
            }
        }catch (Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            attributes.addFlashAttribute("venta", dto);
            redirect.setUrl("/ventas/form");
            e.printStackTrace();
        }
        return redirect;
    }


    @GetMapping("/delete/{id}")
    public RedirectView delete(@PathVariable Long id, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/ventas");
        ventaServicio.eliminarPorId(id);
        attributes.addFlashAttribute("exito", "Se ha eliminado correctamente el registro");
        return redirect;
    }


    @GetMapping("/obtenerTotalPorId/{id}")
    public ResponseEntity<Double> obtenerTotalPorId(@PathVariable Long id){
        return ResponseEntity.ok(ventaServicio.obtenerTotalPorId(id));
    }

    @PostMapping("/actualizarTotalVenta/{idVenta}")
    public ResponseEntity<String> actualizarTotal(@PathVariable Long idVenta,RedirectAttributes attributes){
        try{
            ventaServicio.actualizarTotalNuevo(idVenta);
            attributes.addFlashAttribute("exito", "Se guardaron los cambios de detalle correctamente");
        }catch(Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            System.out.println(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Registro actualizado exitosamente");
    }


    @GetMapping("/validarEstado/{idVenta}")
    public ResponseEntity<Boolean> validarEstado(@PathVariable Long idVenta){
        return  ResponseEntity.ok(ventaServicio.validarEstado(idVenta));
    }

    @GetMapping("/obtenerVentasSinCreditoPorCliente/{id}")
    public ResponseEntity<List<Venta>> obtenerVentasSinCreditoPorCliente(@PathVariable Long id){
        return ResponseEntity.ok(ventaServicio.obtenerVentasSinCreditoPorCliente(id));
    }

    @GetMapping("/obtenerIndiceBase/{idVenta}")
    public ResponseEntity<BigDecimal> obtenerIndiceBase(@PathVariable Long idVenta){
        return ResponseEntity.ok(ventaServicio.obtenerIndiceBase(idVenta));
    }



}
