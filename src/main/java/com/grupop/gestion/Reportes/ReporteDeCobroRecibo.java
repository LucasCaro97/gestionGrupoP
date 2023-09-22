package com.grupop.gestion.Reportes;

import com.grupop.gestion.Configuracion.ConvertidorNumATexto;
import com.grupop.gestion.DTO.CobroDetalleCuotasReporteDto;
import com.grupop.gestion.DTO.FormaDePagoDetalleDTO;
import com.grupop.gestion.Entidades.Cobro;;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class ReporteDeCobroRecibo {

    private Cobro cobro;

    private String cliente;
    private List<CobroDetalleCuotasReporteDto> listaItems;
    private List<FormaDePagoDetalleDTO> listaDetallePago;


    public ReporteDeCobroRecibo(Cobro cobro, String cliente, List<CobroDetalleCuotasReporteDto> listaItems, List<FormaDePagoDetalleDTO> listaDetallePago) {
        this.cobro = cobro;
        this.listaItems = listaItems;
        this.cliente = cliente;
        this.listaDetallePago = listaDetallePago;
    }


    public ResponseEntity<byte[]> exportInvoice(){
        try{

            InputStream jasperStream = Cobro.class.getResourceAsStream("/static/reportes/ReciboCobro.jasper");
            CobroDetalleCuotasReporteDto dummyEntidad = new CobroDetalleCuotasReporteDto();
            dummyEntidad.setDescripcion(null);
            dummyEntidad.setIntereses(BigDecimal.ZERO);
            dummyEntidad.setTotal(BigDecimal.ZERO);
            listaItems.add(0, dummyEntidad);
            BigDecimal subtotal = BigDecimal.ZERO;

            String nroRecibo = cobro.getTalonario() + "-" + cobro.getNroComprobante();

            JRDataSource dataSource = new JRBeanCollectionDataSource(listaItems);
            JRDataSource dataSourceMediosPago = new JRBeanCollectionDataSource(listaDetallePago);

            InputStream logoStream = Cobro.class.getResourceAsStream("/static/img/logo_grupop.png");

            for (CobroDetalleCuotasReporteDto c : listaItems ) {
                subtotal = subtotal.add(c.getTotal());
            }



            Map<String, Object> parametros = new HashMap<>();
            parametros.put("logoEmpresa", logoStream);
            parametros.put("ds", dataSource);
            parametros.put("cliente", cliente);
            parametros.put("nroComprobante", nroRecibo);
            parametros.put("idCobro", cobro.getId());
            parametros.put("dataSourceMediosPago", dataSourceMediosPago);
            parametros.put("recibimos", ConvertidorNumATexto.cantidadConLetra(subtotal.toString()));
            parametros.put("total", subtotal);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperStream, parametros, dataSource);

            byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", generarNombreArchivo(cobro.getId()));
            return ResponseEntity.ok().headers(headers).body(pdfBytes);

        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }



    }



    public static String generarNombreArchivo(Long idCobro){
        LocalDateTime fechaHoraActual = LocalDateTime.now();
        DateTimeFormatter formateador = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String fechaHoraFormateada = fechaHoraActual.format(formateador);
        return "reciboCobro_"+ idCobro+ "_"  + fechaHoraFormateada + ".pdf";
    }








}
