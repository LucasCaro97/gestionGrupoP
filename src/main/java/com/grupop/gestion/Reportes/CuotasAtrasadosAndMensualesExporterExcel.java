package com.grupop.gestion.Reportes;


import com.grupop.gestion.DTO.CobrosDTO;
import com.grupop.gestion.DTO.CreditoDetalleDto;
import com.grupop.gestion.Entidades.CreditoDetalle;
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
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class CuotasAtrasadosAndMensualesExporterExcel {

    private XSSFWorkbook libro;
    private XSSFSheet hoja;
    private List<CreditoDetalleDto> listaCuotas;

    public CuotasAtrasadosAndMensualesExporterExcel(List<CreditoDetalleDto> listaCuotas) {
        this.listaCuotas = listaCuotas;
        libro = new XSSFWorkbook();
        hoja = libro.createSheet("Cuotas a Cobrar");
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
        celda.setCellValue("Cliente");
        celda.setCellStyle(estilo);

        celda = fila.createCell(2);
        celda.setCellValue("Credito");
        celda.setCellStyle(estilo);

        celda = fila.createCell(3);
        celda.setCellValue("Nro Cuota");
        celda.setCellStyle(estilo);

        celda = fila.createCell(4);
        celda.setCellValue("Vencimiento");
        celda.setCellStyle(estilo);

        celda = fila.createCell(5);
        celda.setCellValue("Capital");
        celda.setCellStyle(estilo);

        celda = fila.createCell(6);
        celda.setCellValue("Gasto Adm");
        celda.setCellStyle(estilo);

        celda = fila.createCell(7);
        celda.setCellValue("Monto");
        celda.setCellStyle(estilo);

        celda = fila.createCell(8);
        celda.setCellValue("Saldo");
        celda.setCellStyle(estilo);

        celda = fila.createCell(9);
        celda.setCellValue("Dias Atrasados");
        celda.setCellStyle(estilo);

        celda = fila.createCell(10);
        celda.setCellValue("Interes Pun");
        celda.setCellStyle(estilo);

        celda = fila.createCell(11);
        celda.setCellValue("Indice Cac");
        celda.setCellStyle(estilo);

        celda = fila.createCell(12);
        celda.setCellValue("Total");
        celda.setCellStyle(estilo);
    }


    private void escribirDatosDeLaTabla(){
        int numeroFilas = 1;
        CellStyle estilo = libro.createCellStyle();
        XSSFFont fuente = libro.createFont();
        fuente.setFontHeight(14);
        estilo.setFont(fuente);

        for (CreditoDetalleDto c: listaCuotas) {
            Row fila = hoja.createRow(numeroFilas++);

            Cell celda = fila.createCell(0);
            celda.setCellValue(c.getId());
            hoja.autoSizeColumn(0);
            celda.setCellStyle(estilo);

            celda = fila.createCell(1);
            celda.setCellValue(c.getCliente());
            hoja.autoSizeColumn(1);
            celda.setCellStyle(estilo);

            celda = fila.createCell(2);
            celda.setCellValue(c.getIdCredito());
            hoja.autoSizeColumn(2);
            celda.setCellStyle(estilo);

            celda = fila.createCell(3);
            celda.setCellValue(c.getNroCuota());
            hoja.autoSizeColumn(3);
            celda.setCellStyle(estilo);

            celda = fila.createCell(4);
            celda.setCellValue(c.getVencimiento().toString());
            hoja.autoSizeColumn(4);
            celda.setCellStyle(estilo);

            celda = fila.createCell(5);
            celda.setCellValue(c.getCapital().doubleValue());
            hoja.autoSizeColumn(5);
            celda.setCellStyle(estilo);

            celda = fila.createCell(6);
            celda.setCellValue(c.getGastoAdm().doubleValue());
            hoja.autoSizeColumn(6);
            celda.setCellStyle(estilo);

            celda = fila.createCell(7);
            celda.setCellValue(c.getMonto().doubleValue());
            hoja.autoSizeColumn(7);
            celda.setCellStyle(estilo);

            celda = fila.createCell(8);
            celda.setCellValue(c.getSaldo().doubleValue());
            hoja.autoSizeColumn(8);
            celda.setCellStyle(estilo);

            celda = fila.createCell(9);
            celda.setCellValue(c.getDiasAtrasados());
            hoja.autoSizeColumn(9);
            celda.setCellStyle(estilo);

            celda = fila.createCell(10);
            celda.setCellValue(c.getInteresPun().doubleValue());
            hoja.autoSizeColumn(10);
            celda.setCellStyle(estilo);

            celda = fila.createCell(11);
            celda.setCellValue(c.getAjusteCac().doubleValue());
            hoja.autoSizeColumn(11);
            celda.setCellStyle(estilo);

            celda = fila.createCell(12);
            celda.setCellValue(c.getTotal().doubleValue());
            hoja.autoSizeColumn(12);
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
