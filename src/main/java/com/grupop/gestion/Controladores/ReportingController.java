package com.grupop.gestion.Controladores;

import com.grupop.gestion.Entidades.CreditoDetalle;
import com.grupop.gestion.Reportes.*;
import com.grupop.gestion.Servicios.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Controller
@RequestMapping("/reporting")
@RequiredArgsConstructor
public class ReportingController {

    private final TipoOperacionServicio tipoOperacionServicio;
    private final SectorServicio sectorServicio;
    private final FormaDePagoServicio formaDePagoServicio;
    private final VentaServicio ventaServicio;
    private final CobroServicio cobroServicio;
    private final CompraServicio compraServicio;
    private final PagoServicio pagoServicio;
    private final CreditoServicio creditoServicio ;


    @GetMapping("operaciones")
    public ModelAndView getForm(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("form-reporteOperaciones");
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if(inputFlashMap!=null){
            mav.addObject("exception", inputFlashMap.get("exception"));
            mav.addObject("reporte", inputFlashMap.get("reporte"));
        }
        mav.addObject("listaOperaciones", tipoOperacionServicio.obtenerTodos());
        mav.addObject("listaSector", sectorServicio.obtenerTodos());
        return mav;
    }

    @GetMapping("adelantos")
    public ModelAndView getFormAdelantos(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("form-reporteAdelantos");
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if(inputFlashMap!=null){
            mav.addObject("exception", inputFlashMap.get("exception"));
            mav.addObject("reporte", inputFlashMap.get("reporte"));
        }
        mav.addObject("listaOperaciones", tipoOperacionServicio.obtenerTodos());
        mav.addObject("listaSector", sectorServicio.obtenerTodos());
        return mav;
    }



    @PostMapping("/generarExcelOperaciones")
    public void generarExcel(@RequestParam Map<String, Object> params, HttpServletResponse response) throws IOException {
        response.setContentType("application/octec-stream");
        DateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String fechaActual = dateFormater.format(new Date());
        String cabecera = "Content-Disposition";


        Long tipoOp = (params.get("tipoOperacion") != null) ? Long.valueOf(params.get("tipoOperacion").toString()) : null;
        String fechaDesde = (params.get("fechaDesde") != null ) ? params.get("fechaDesde").toString() : null ;
        String fechaHasta = (params.get("fechaHasta") != null ) ?  params.get("fechaHasta").toString() : null ;
        Long sectorId = (params.get("sector") != "" ) ? Long.valueOf(params.get("sector").toString()) : null;
        Integer talDesde = (params.get("talonarioDesde") != "" ) ? Integer.valueOf(params.get("talonarioDesde").toString()) : null;
        Integer talHasta = (params.get("talonarioHasta") != "") ? Integer.valueOf(params.get("talonarioHasta").toString()) : null;
        Boolean excluirTal = params.get("flexCheckDefault") != null;
        Long idFormaPago = (params.get("formaPago") != "") ? Long.valueOf(params.get("formaPago").toString()) : null;

        if(tipoOp == 1){
            String valor = "attachment; filename=ReporteVentas_" + fechaActual + ".xlsx";
            response.setHeader(cabecera,valor);
            VentasExporterExcel exporter = new VentasExporterExcel(ventaServicio.obtenerOperaciones(fechaDesde,fechaHasta,sectorId,talDesde,talHasta,excluirTal,idFormaPago));
            exporter.exportar(response);
        }else if(tipoOp == 2){
            String valor = "attachment; filename=ReporteCompras_" + fechaActual + ".xlsx";
            CompraExporterExcel exporter = new CompraExporterExcel(compraServicio.obtenerOperaciones(fechaDesde,fechaHasta,sectorId,talDesde,talHasta,excluirTal,idFormaPago));
            exporter.exportar(response);
        } else if (tipoOp == 3) {
            String valor = "attachment; filename=ReporteCobros_" + fechaActual + ".xlsx";
            response.setHeader(cabecera,valor);
            CobroExporterExcel exporter = new CobroExporterExcel(cobroServicio.obtenerOperaciones(fechaDesde,fechaHasta,sectorId,talDesde,talHasta,excluirTal,idFormaPago));
            exporter.exportar(response);
        } else if (tipoOp == 4) {
            String valor = "attachment; filename=ReportePagos_" + fechaActual + ".xlsx";
            response.setHeader(cabecera,valor);
            PagoExporterExcel exporter = new PagoExporterExcel(pagoServicio.obtenerOperaciones(fechaDesde,fechaHasta,sectorId,talDesde,talHasta,excluirTal,idFormaPago));
            exporter.exportar(response);
        }
    }


    @GetMapping("/generarExcelAtrasadosAndMensuales")
    public void generarExcelCuotasMensualAndAtrasados(HttpServletResponse response) throws IOException {
        response.setContentType("application/octec-stream");
        DateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String fechaActual = dateFormater.format(new Date());
        String cabecera = "Content-Disposition";
        String valor = "attachment; filename=ReporteCuotasCobrarMensual_" + fechaActual + ".xlsx";
        response.setHeader(cabecera,valor);
        CuotasAtrasadosAndMensualesExporterExcel exporter = new CuotasAtrasadosAndMensualesExporterExcel(creditoServicio.obtenerCuotasCobrarMensual());
        exporter.exportar(response);
    }

}
