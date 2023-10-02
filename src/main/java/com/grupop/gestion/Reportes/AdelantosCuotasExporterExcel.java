package com.grupop.gestion.Reportes;

import com.grupop.gestion.DTO.CobroCuotasDTO;
import com.grupop.gestion.DTO.OperacionesDTO;
import com.grupop.gestion.Entidades.Cobro;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.List;

public class AdelantosCuotasExporterExcel {

    private XSSFWorkbook libro;
    private XSSFSheet hoja;
    private List<CobroCuotasDTO> listaAdelantos;

    public AdelantosCuotasExporterExcel(List<CobroCuotasDTO> listaAdelantos) {
        this.listaAdelantos = listaAdelantos;
        libro = new XSSFWorkbook();
        hoja = libro.createSheet("Cobro Cuotas");
    }


    private void escribirCabeceraDeLaTabla(){
        Row fila = hoja.createRow(0);

        CellStyle estilo = libro.createCellStyle();
        XSSFFont fuente = libro.createFont();
        fuente.setBold(true);
        fuente.setFontHeight(16);
        estilo.setFont(fuente);

        Cell celda = fila.createCell(0);
        celda.setCellValue("Id_Cobro");
        celda.setCellStyle(estilo);


        celda = fila.createCell(1);
        celda.setCellValue("Talonario");
        celda.setCellStyle(estilo);

        celda = fila.createCell(2);
        celda.setCellValue("Cliente");
        celda.setCellStyle(estilo);

        celda = fila.createCell(3);
        celda.setCellValue("Id_Credito");
        celda.setCellStyle(estilo);

        celda = fila.createCell(4);
        celda.setCellValue("Nro_Cuota");
        celda.setCellStyle(estilo);

        celda = fila.createCell(5);
        celda.setCellValue("Plan");
        celda.setCellStyle(estilo);

        celda = fila.createCell(6);
        celda.setCellValue("Fecha Cobro");
        celda.setCellStyle(estilo);

        celda = fila.createCell(7);
        celda.setCellValue("Importe Base");
        celda.setCellStyle(estilo);

        celda = fila.createCell(8);
        celda.setCellValue("Ajuste");
        celda.setCellStyle(estilo);

        celda = fila.createCell(9);
        celda.setCellValue("Intereses");
        celda.setCellStyle(estilo);

        celda = fila.createCell(10);
        celda.setCellValue("Bonificacion");
        celda.setCellStyle(estilo);

        celda = fila.createCell(11);
        celda.setCellValue("Total Cobrado");
        celda.setCellStyle(estilo);

        celda = fila.createCell(12);
        celda.setCellValue("Cancelo");
        celda.setCellStyle(estilo);

    }

    private void escribirDatosDeLaTabla(){
        int numeroFilas = 1;
        CellStyle estilo = libro.createCellStyle();
        XSSFFont fuente = libro.createFont();
        fuente.setFontHeight(14);
        estilo.setFont(fuente);

        for(CobroCuotasDTO c : listaAdelantos){
            Row fila = hoja.createRow(numeroFilas++);

            Cell celda = fila.createCell(0);
            celda.setCellValue(c.getIdCobro());
            hoja.autoSizeColumn(0);
            celda.setCellStyle(estilo);

            celda = fila.createCell(1);
            celda.setCellValue(c.getTalonario());
            hoja.autoSizeColumn(1);
            celda.setCellStyle(estilo);

            celda = fila.createCell(2);
            celda.setCellValue(c.getClienteId());
            hoja.autoSizeColumn(2);
            celda.setCellStyle(estilo);

            celda = fila.createCell(3);
            celda.setCellValue(c.getCreditoId());
            hoja.autoSizeColumn(3);
            celda.setCellStyle(estilo);

            celda = fila.createCell(4);
            celda.setCellValue(c.getNroCuota());
            hoja.autoSizeColumn(4);
            celda.setCellStyle(estilo);

            celda = fila.createCell(5);
            celda.setCellValue(c.getPlanPago());
            hoja.autoSizeColumn(5);
            celda.setCellStyle(estilo);

            celda = fila.createCell(6);
            celda.setCellValue(c.getFechaCobro().toString());
            hoja.autoSizeColumn(6);
            celda.setCellStyle(estilo);

            celda = fila.createCell(7);
            celda.setCellValue(c.getImporteCuota().doubleValue());
            hoja.autoSizeColumn(7);
            celda.setCellStyle(estilo);

            celda = fila.createCell(8);
            celda.setCellValue(c.getImporteAjuste().doubleValue());
            hoja.autoSizeColumn(8);
            celda.setCellStyle(estilo);

            celda = fila.createCell(9);
            celda.setCellValue(c.getImporteIntereses().doubleValue());
            hoja.autoSizeColumn(9);
            celda.setCellStyle(estilo);

            celda = fila.createCell(10);
            celda.setCellValue(c.getImporteBonificacion().doubleValue());
            hoja.autoSizeColumn(10);
            celda.setCellStyle(estilo);

            celda = fila.createCell(11);
            celda.setCellValue(c.getImporteACobrar().doubleValue());
            hoja.autoSizeColumn(11);
            celda.setCellStyle(estilo);

            celda = fila.createCell(12);
            celda.setCellValue((c.getCancelo()) ? 1 : 0);
            hoja.autoSizeColumn(12);
            celda.setCellStyle(estilo);

        }
    }

    public void exportar(HttpServletResponse response) throws IOException {
        try{
            escribirCabeceraDeLaTabla();
            escribirDatosDeLaTabla();

            ServletOutputStream outputStream = response.getOutputStream();
            libro.write(outputStream);
            libro.close();
            outputStream.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
