package com.grupop.gestion.Controladores;

import com.grupop.gestion.Entidades.Impuestos;
import com.grupop.gestion.Entidades.Manzana;
import com.grupop.gestion.Entidades.Producto;
import com.grupop.gestion.Entidades.Venta;
import com.grupop.gestion.Servicios.*;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
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
@RequestMapping("/producto")
public class ProductoControlador {

    private final ProductoServicio productoServicio;
    private final TipoProductoServicio tipoProductoServicio;
    private final CuentasContablesServicio cuentasContablesServicio;
    private final ImpuestosServicio impuestosServicio;
    private final LoteServicio loteServicio;


    @GetMapping
    public ModelAndView getAll(@RequestParam Map<String, Object> params , HttpServletRequest request, @Param("tipoProd") Long tipoProd, @Param("cuenta") Long cuenta, @Param("descripcion") String descripcion){
        ModelAndView mav = new ModelAndView("tabla-producto");
        Map<String,?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if (inputFlashMap!=null){ mav.addObject("exito", inputFlashMap.get("exito")); }

        int page = params.get("page") != null ? ( Integer.valueOf(params.get("page").toString()) -1) : 0;
        Page<Producto> pageProducto = productoServicio.obtenerTodos(descripcion,tipoProd,cuenta,page, 50);
        int totalPage = pageProducto.getTotalPages();
        if(totalPage > 0){
            List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
            mav.addObject("pages", pages);
        }

        mav.addObject("listaProd", pageProducto.getContent());
        mav.addObject("current", page + 1);
        mav.addObject("next", page + 2);
        mav.addObject("prev", page);
        mav.addObject("last", totalPage);
        mav.addObject("object", "producto");

        mav.addObject("listaTipoProd", tipoProductoServicio.obtenerTodos());
        mav.addObject("listaCuentas", cuentasContablesServicio.obtenerTodos(52l));
        return mav;
    }

    @GetMapping("/obtenerPorDescripcionAndCuenta/{descripcion}/{idCuenta}")
    public ResponseEntity<List<Producto>> obtenerProductoPorDescAndCuenta(@PathVariable String descripcion, @PathVariable Long idCuenta){
        return ResponseEntity.ok(productoServicio.obtenerPorDescripcionAndCuenta(descripcion,idCuenta));
    }

    @GetMapping("/obtenerPorDescripcion/{descripcion}")
    public ResponseEntity<List<Producto>> obtenerProductoPorDesc(@PathVariable String descripcion){
        return ResponseEntity.ok(productoServicio.obtenerPorDescripcion(descripcion));
    }

    @GetMapping("/obtenerPorCuenta/{idCuenta}")
    public ResponseEntity<List<Producto>> obtenerProductoPorCuenta(@PathVariable Long idCuenta){
        return ResponseEntity.ok(productoServicio.obtenerPorCuenta(idCuenta));
    }


    @GetMapping("/obtenerProductosTodos")
    public ResponseEntity<List<Producto>> obtenerProductosTodos(){
        return ResponseEntity.ok(productoServicio.obtenerTodos());
    }

    @GetMapping("/form")
    public ModelAndView getForm(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("form-producto");
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);

        if (inputFlashMap != null) {
            mav.addObject("exception", inputFlashMap.get("exception"));
            mav.addObject("producto", inputFlashMap.get("producto"));
        }else{
            mav.addObject("producto", new Producto());
        }
        mav.addObject("listaTipoProd", tipoProductoServicio.obtenerTodos());
        mav.addObject("listaCuentas", cuentasContablesServicio.obtenerTodos());
        mav.addObject("listaImpuestos", impuestosServicio.obtenerTodos());
        mav.addObject("action", "create");
        return mav;
    }

    @GetMapping("/form/{id}")
    public ModelAndView getFormUpd(@PathVariable Long id){
        ModelAndView mav = new ModelAndView("form-producto");
        mav.addObject("producto", productoServicio.buscarPorId(id));
        mav.addObject("action", "update");
        mav.addObject("listaTipoProd", tipoProductoServicio.obtenerTodos());
        mav.addObject("listaCuentas", cuentasContablesServicio.obtenerTodos());
        mav.addObject("listaImpuestos", impuestosServicio.obtenerTodos());
        return mav;
    }

    @PostMapping("/create")
    public RedirectView create(Producto dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/producto");
        try{
            productoServicio.crear(dto);
            attributes.addFlashAttribute("exito", "Se ha creado el producto correctamente");
        }catch (Exception e){
            attributes.addFlashAttribute("producto", dto);
            attributes.addFlashAttribute("exception", e.getMessage());
            attributes.addFlashAttribute("etapa", "etapa2");
            System.out.println("Redireccionando al formulario");
        }

        return redirect;
    }

    @PostMapping("update")
    RedirectView update(Producto dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/producto");
        try {
            productoServicio.actualizar(dto);
            attributes.addFlashAttribute("exito", "Se ha actualizado el producto correctamente");
        }catch (Exception e){
            attributes.addFlashAttribute("exception", e.getMessage());
            attributes.addFlashAttribute("producto", dto);
            redirect.setUrl("/producto/form");
        }
        return redirect;
    }
    @GetMapping("delete/{id}")
    RedirectView delete(@PathVariable Long id, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/producto");
        Producto p = productoServicio.buscarPorId(id);

        if(p.getTipoProducto().getId()==2){
            loteServicio.bajaProducto(p);
        }
        productoServicio.eliminarPorId(id);
        attributes.addFlashAttribute("exito", "Se ha eliminado correctamente el producto");
        return redirect;
    }



}
