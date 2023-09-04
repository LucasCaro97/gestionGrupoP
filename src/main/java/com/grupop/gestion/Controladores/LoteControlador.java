package com.grupop.gestion.Controladores;

import com.grupop.gestion.Entidades.Lote;
import com.grupop.gestion.Servicios.EstadoLoteServicio;
import com.grupop.gestion.Servicios.LoteServicio;
import com.grupop.gestion.Servicios.ManzanaServicio;
import com.grupop.gestion.Servicios.UrbanizacionServicio;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/lote")
public class LoteControlador {

    private final LoteServicio loteServicio;
    private final ManzanaServicio  manzanaServicio;
    private final UrbanizacionServicio urbanizacionServicio;
    private final EstadoLoteServicio estadoLoteServicio;

    @GetMapping
    public ModelAndView getAll(HttpServletRequest request,  @Param("urbanizacion") Long urbanizacion, @Param("manzana") Long manzana){
        ModelAndView mav = new ModelAndView("tabla-lote");
        Map<String,?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if(inputFlashMap!=null) {
            if(inputFlashMap.containsKey("exito")){
                mav.addObject("exito", inputFlashMap.get("exito"));
            }else{
                mav.addObject("exception", inputFlashMap.get("exception"));
            }
        }
        mav.addObject("listaUrbs", urbanizacionServicio.obtenerTodos());
        mav.addObject("listaLotes",loteServicio.obtenerTodos(urbanizacion, manzana));
        return mav;
    }

    @GetMapping("/form")
    public ModelAndView getForm(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("form-lote");
        Map<String,?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if(inputFlashMap!=null){
            mav.addObject("lote", inputFlashMap.get("lote"));
            mav.addObject("exception", inputFlashMap.get("exception"));
        }else {
            mav.addObject("lote", new Lote());
        }
        mav.addObject("action", "create");
        mav.addObject("listaUrbs", urbanizacionServicio.obtenerTodos());
        mav.addObject("listaManzanas", manzanaServicio.obtenerTodos());
        mav.addObject("listaEstadoLote", estadoLoteServicio.obtenerTodos());
        return mav;
    }


    @GetMapping("/form/{id}")
    public ModelAndView getFormUpd(@PathVariable Long id){
        ModelAndView mav = new ModelAndView("form-lote");
        mav.addObject("lote", loteServicio.obtenerPorId(id));
        mav.addObject("listaUrbs", urbanizacionServicio.obtenerTodos());
        mav.addObject("listaEstadoLote", estadoLoteServicio.obtenerTodos());
        mav.addObject("listaManzana", manzanaServicio.obtenerTodos());
        mav.addObject("action", "update");
        return mav;
    }

    @PostMapping("create")
    public RedirectView create(Lote dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/lote");
        try{
            loteServicio.crear(dto);
            attributes.addFlashAttribute("exito", "Se ha creado correctamente la lote");
        }catch(Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            attributes.addFlashAttribute("lote", dto);
            redirect.setUrl("/lote/form");
        }
        return redirect;
    }

    @PostMapping("update")
    public RedirectView update(Lote dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/lote");
        try{
            loteServicio.actualizar(dto);
            attributes.addFlashAttribute("exito", "Se ha actualizado correctamente la lote");
        }catch(Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            attributes.addFlashAttribute("lote", dto);
            redirect.setUrl("/lote/form");
        }
        return redirect;
    }


    @GetMapping("/delete/{id}")
    public RedirectView delete(@PathVariable Long id, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/lote");
        loteServicio.eliminarPorId(id);
        attributes.addFlashAttribute("exito", "Se ha eliminado correctamente la lote");
        return redirect;
    }

    @GetMapping("/altaProducto/{id}")
    public RedirectView altaProductoLote(@PathVariable Long id, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/lote");
        try{
            loteServicio.altaComoProducto(id);
            attributes.addFlashAttribute("exito", "Se dio de alta el lote correctamente como producto");
        }catch (Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
        }
        return redirect;
    }

    @GetMapping("/altaProductoGrupo/{id}")
    public RedirectView altaProductoGrupo(@PathVariable Long id, RedirectAttributes attributes) {
        RedirectView redirect = new RedirectView("/urbanizacion");
        try{
            loteServicio.altaGrupoProducto(id);
            attributes.addFlashAttribute("exito", "Se dio de alta el grupo de lotes como producto correctamente");
        } catch (Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
        }
        return redirect;
    }

    @PostMapping("/setEstadoVendido/{id}")
    public ResponseEntity<String> setEstadoVendido(@PathVariable Long id){
        try{
            loteServicio.alterarEstado(id, 3l);
        }catch(Exception e){
            System.out.println("Excepcion al marcar lote como vendido");
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Se altero el estado del lote correctamente");
    }

    @PostMapping("/setEstadoDisponible/{id}")
    public ResponseEntity<String> setEstadoDisponible(@PathVariable Long id){
        try{
            loteServicio.alterarEstado(id, 1l);
        }catch(Exception e){
            System.out.println("Excepcion al marcar lote como disponible");
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Se altero el estado del lote correctamente");
    }

    @GetMapping("/obtenerStockPorUrb/{id}")
    public ResponseEntity<Integer> obtenerStockPorUrb(@PathVariable Long id){
        return ResponseEntity.ok(loteServicio.obtenerStockPorUrb(id));
    }
    @GetMapping("/obtenerDisponiblesPorUrb/{id}")
    public ResponseEntity<Integer> obtenerDisponiblesPorUrb(@PathVariable Long id){
        return ResponseEntity.ok(loteServicio.obtenerDisponiblesPorUrb(id));
    }
    @GetMapping("/obtenerVendidosPorUrb/{id}")
    public ResponseEntity<Integer> obtenerVendidosPorUrb(@PathVariable Long id){
        return ResponseEntity.ok(loteServicio.obtenerVendidosPorUrb(id));
    }

    @PostMapping("crearLotesPorGrupo/{idUrb}/{idManzana}/{cantLotes}")
    public ResponseEntity<String> crearLotesPorGrupo(@PathVariable Long idUrb, @PathVariable Long idManzana, @PathVariable Integer cantLotes){
        loteServicio.crearGrupoDeLotes(idUrb, idManzana, cantLotes);
        return null;
    }



}
