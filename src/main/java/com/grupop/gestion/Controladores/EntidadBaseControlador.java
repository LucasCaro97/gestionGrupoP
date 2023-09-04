package com.grupop.gestion.Controladores;

import com.grupop.gestion.Entidades.EntidadBase;
import com.grupop.gestion.Reportes.EntidadBaseExporterExcel;
import com.grupop.gestion.Reportes.EntidadBaseExporterPDF;
import com.grupop.gestion.Servicios.EntidadBaseServicio;
import com.grupop.gestion.Servicios.TipoIvaServicio;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/entidadBase")
public class EntidadBaseControlador {

    private final EntidadBaseServicio entidadBaseServicio;
    private final TipoIvaServicio tipoIvaServicio;

    @GetMapping
    public ModelAndView getAll(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("tabla-entidadBase");
        Map<String,?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);

        if(inputFlashMap != null) mav.addObject("exito", inputFlashMap.get("exito"));
        mav.addObject("listaEntidad", entidadBaseServicio.obtenerTodos());
        return mav;
    }

    @GetMapping("/clientes")
    public ModelAndView getAllClients(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("tabla-entidadBase");
        Map<String,?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);

        if(inputFlashMap != null) mav.addObject("exito", inputFlashMap.get("exito"));
        mav.addObject("listaEntidad", entidadBaseServicio.obtenerClientes());
        return mav;
    }

    @GetMapping("/proveedores")
    public ModelAndView getAllSuppliers(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("tabla-entidadBase");
        Map<String,?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);

        if(inputFlashMap != null) mav.addObject("exito", inputFlashMap.get("exito"));
        mav.addObject("listaEntidad", entidadBaseServicio.obtenerProveedores());
        return mav;
    }

    @GetMapping("/empleados")
    public ModelAndView getAllEmployees(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("tabla-entidadBase");
        Map<String,?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);

        if(inputFlashMap != null) mav.addObject("exito", inputFlashMap.get("exito"));
        mav.addObject("listaEntidad", entidadBaseServicio.obtenerEmpleados());
        return mav;
    }

    @GetMapping("/vendedores")
    public ModelAndView getAllSellers(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("tabla-entidadBase");
        Map<String,?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);

        if(inputFlashMap != null) mav.addObject("exito", inputFlashMap.get("exito"));
        mav.addObject("listaEntidad", entidadBaseServicio.obtenerVendedores());
        return mav;
    }

    @GetMapping("/form")
    public ModelAndView getForm(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("form-entidadBase");
        Map<String,?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);

        if(inputFlashMap != null){
            mav.addObject("entidad", inputFlashMap.get("entidad"));
            mav.addObject("exception", inputFlashMap.get("exception"));
        }else {
            mav.addObject("entidad", new EntidadBase());
            mav.addObject("listaEntidad", entidadBaseServicio.obtenerTodos());
            mav.addObject("listaIva", tipoIvaServicio.obtenerTodos());
        }
        mav.addObject("action", "create");
        return mav;
    }

    @GetMapping("/form/{id}")
    public ModelAndView getFormUpdate(@PathVariable Long id){
        ModelAndView mav = new ModelAndView("form-EntidadBase");
        mav.addObject("entidad", entidadBaseServicio.buscarPorId(id));
        mav.addObject("action", "update");
        mav.addObject("listaEntidad", entidadBaseServicio.obtenerTodos());
        mav.addObject("listaIva", tipoIvaServicio.obtenerTodos());
        return mav;
    }
    @PostMapping("/create")
    public RedirectView create(EntidadBase dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/entidadBase");
        try{
            entidadBaseServicio.crear(dto);
            attributes.addFlashAttribute("exito", "La operacion se ha realizado con exito");
        } catch (IllegalArgumentException e){
            attributes.addFlashAttribute("entidadBase", dto);
            attributes.addFlashAttribute("exception", e.getMessage());
            redirect.setUrl("/entidadBase/form");
        }
        return redirect;
    }


    @PostMapping("/update")
    public RedirectView update(EntidadBase dto, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/entidadBase");

        try{
            entidadBaseServicio.actualizar(dto);
            attributes.addFlashAttribute("exito", "La operacion se ha realizado con exito");
        }catch(IllegalArgumentException e){
            attributes.addFlashAttribute("entidad", dto);
            attributes.addFlashAttribute("exception", e.getMessage());
            redirect.setUrl("/entidadBase/form");
        }
        return redirect;
    }

    @GetMapping("/delete/{id}")
    public RedirectView deleteById(@PathVariable Long id, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/entidadBase");
        entidadBaseServicio.eliminarPorId(id);
        attributes.addFlashAttribute("exito", "La operacion se ha realizado con exito");
        return redirect;
    }

    @GetMapping("/setClient/{id}")
    public RedirectView setClient(@PathVariable Long id, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/entidadBase");
        entidadBaseServicio.setCliente(id);
        attributes.addFlashAttribute("exito", "Se ha asignado como cliente a la entidad nro:" + id);
        return redirect;
    }

    @GetMapping("/setProv/{id}")
    public RedirectView setProv(@PathVariable Long id, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/entidadBase");
        entidadBaseServicio.setProv(id);
        attributes.addFlashAttribute("exito", "Se ha asignado como proveedor a la entidad nro:" + id);
        return redirect;
    }

    @GetMapping("/setEmp/{id}")
    public RedirectView setEmp(@PathVariable Long id, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/entidadBase");
        entidadBaseServicio.setEmp(id);
        attributes.addFlashAttribute("exito", "Se ha asignado como empleado a la entidad nro:" + id);
        return redirect;
    }

    @GetMapping("/setVend/{id}")
    public RedirectView setVend(@PathVariable Long id, RedirectAttributes attributes){
        RedirectView redirect = new RedirectView("/entidadBase");
        entidadBaseServicio.setVend(id);
        attributes.addFlashAttribute("exito", "Se ha asignado como vendedor a la entidad nro:" + id);
        return redirect;
    }

    @GetMapping("/obtenerCuit/{id}")
    public ResponseEntity<String> obtenerCuitCliente(@PathVariable Long id){
        //System.out.println(entidadBaseServicio.obtenerCuitCliente(id));
        return ResponseEntity.ok(entidadBaseServicio.obtenerCuitCliente(id));
    }

    @GetMapping("/obtenerIva/{id}")
    public ResponseEntity<String> obtenerIvaCliente(@PathVariable Long id){
        //System.out.println(entidadBaseServicio.obtenerIvaCliente(id));
        return ResponseEntity.ok(entidadBaseServicio.obtenerIvaCliente(id));
    }

    @GetMapping("/obtenerNombrePorFkCliente/{id}")
    public ResponseEntity<EntidadBase> obtenerNombreCliente(@PathVariable Long id){
        //System.out.println(entidadBaseServicio.obtenerNombrePorFkCliente(id));
        return ResponseEntity.ok(entidadBaseServicio.obtenerNombrePorFkCliente(id));
    }
    @GetMapping("/obtenerNombrePorFkProveedor/{id}")
    public ResponseEntity<EntidadBase> obtenerNombreProveedor(@PathVariable Long id){
        //System.out.println(entidadBaseServicio.obtenerNombrePorFkCliente(id));
        return ResponseEntity.ok(entidadBaseServicio.obtenerNombrePorFkProveedor(id));
    }

    @GetMapping("/obtenerNombrePorFkVendedor/{id}")
    public ResponseEntity<EntidadBase> obtenerNombreVendedor(@PathVariable Long id){
        //System.out.println(entidadBaseServicio.obtenerNombrePorFkCliente(id));
        return ResponseEntity.ok(entidadBaseServicio.obtenerNombrePorFkVendedor(id));
    }

    @GetMapping("/generarReporte")
    public ModelAndView exportInvoice( ){

        ModelAndView mav = new ModelAndView("tabla-entidadBase");
        mav.addObject("listaEntidad", entidadBaseServicio.obtenerTodos());
        entidadBaseServicio.exportInvoice();
        return mav;
    }

    @GetMapping("/exportarPDF")
    public void exportarListadoEntidades(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String fechaActual = dateFormater.format(new Date());
        String cabecera = "Content-Disposition";
        String valor = "attachment; filename=Entidades_" + fechaActual + ".pdf";
        response.setHeader(cabecera,valor);
        List<EntidadBase> entidades = entidadBaseServicio.obtenerTodos();

        EntidadBaseExporterPDF exporter = new EntidadBaseExporterPDF(entidades);
        exporter.exportar(response);

    }

    @GetMapping("/exportarExcel")
    public void exportarListadoExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octec-stream");
        DateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String fechaActual = dateFormater.format(new Date());
        String cabecera = "Content-Disposition";
        String valor = "attachment; filename=Entidades_" + fechaActual + ".xlsx";
        response.setHeader(cabecera,valor);
        List<EntidadBase> entidades = entidadBaseServicio.obtenerTodos();

        EntidadBaseExporterExcel exporter = new EntidadBaseExporterExcel(entidades);
        exporter.exportar(response);

    }


}
