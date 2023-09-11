package com.grupop.gestion.Reportes;


import com.grupop.gestion.DTO.PagoDTO;
import com.grupop.gestion.DTO.VentasDTO;
import com.grupop.gestion.Entidades.Pago;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class PagoExporterExcel {

    private XSSFWorkbook libro;
    private XSSFSheet hoja;
    private List<PagoDTO> listaPagos;

    public PagoExporterExcel(List<PagoDTO> listaPagos) {
        this.listaPagos = listaPagos;
        libro = new XSSFWorkbook();
        hoja = libro.createSheet("Pagos");
    }


    private void escribirCabeceraDeLaTabla(){
        Row fila = hoja.createRow(0);

        CellStyle estilo = libro.createCellStyle();
        XSSFFont fuente = libro.createFont();
        fuente.setBold(true);
        fuente.setFontHeight(16);
        estilo.setFont(fuente);

        Cell celda = fila.createCell(0);
        celda.setCellValue("ID");
        celda.setCellStyle(estilo);

        celda = fila.createCell(1);
        celda.setCellValue("Proveedor");
        celda.setCellStyle(estilo);

        celda = fila.createCell(2);
        celda.setCellValue("Fecha Comprobante");
        celda.setCellStyle(estilo);

        celda = fila.createCell(3);
        celda.setCellValue("Talonario");
        celda.setCellStyle(estilo);

        celda = fila.createCell(4);
        celda.setCellValue("Nro Comprobante");
        celda.setCellStyle(estilo);

        celda = fila.createCell(5);
        celda.setCellValue("Sector");
        celda.setCellStyle(estilo);

        celda = fila.createCell(6);
        celda.setCellValue("Forma de Pago");
        celda.setCellStyle(estilo);

        celda = fila.createCell(7);
        celda.setCellValue("Total");
        celda.setCellStyle(estilo);

    }

    private void escribirDatosDeLaTabla(){
        int numeroFilas = 1;
        CellStyle estilo = libro.createCellStyle();
        XSSFFont fuente = libro.createFont();
        fuente.setFontHeight(14);
        estilo.setFont(fuente);

        for(PagoDTO p : listaPagos){
            Row fila = hoja.createRow(numeroFilas++);

            Cell celda = fila.createCell(0);
            celda.setCellValue(p.getId());
            hoja.autoSizeColumn(0);
            celda.setCellStyle(estilo);

            celda = fila.createCell(1);
            celda.setCellValue(p.getProveedor());
            hoja.autoSizeColumn(1);
            celda.setCellStyle(estilo);

            celda = fila.createCell(2);
            celda.setCellValue(p.getFechaComprobante().toString());
            hoja.autoSizeColumn(2);
            celda.setCellStyle(estilo);

            celda = fila.createCell(3);
            celda.setCellValue(p.getTalonario());
            hoja.autoSizeColumn(3);
            celda.setCellStyle(estilo);

            celda = fila.createCell(4);
            celda.setCellValue(p.getNroComprobante());
            hoja.autoSizeColumn(4);
            celda.setCellStyle(estilo);

            celda = fila.createCell(5);
            celda.setCellValue(p.getSector());
            hoja.autoSizeColumn(5);
            celda.setCellStyle(estilo);

            celda = fila.createCell(6);
            celda.setCellValue(p.getFormaPago());
            hoja.autoSizeColumn(6);
            celda.setCellStyle(estilo);

            celda = fila.createCell(7);
            celda.setCellValue(p.getTotal().toString());
            hoja.autoSizeColumn(7);
            celda.setCellStyle(estilo);

        }
    }

    public void exportar(HttpServletResponse response) throws IOException {
        escribirCabeceraDeLaTabla();
        escribirDatosDeLaTabla();

        ServletOutputStream outputStream = response.getOutputStream();
        libro.write(outputStream);
        libro.close();
        outputStream.close();
    }

}
