package com.grupop.gestion.Reportes;

import com.grupop.gestion.Entidades.EntidadBase;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFShape;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.List;

public class EntidadBaseExporterExcel {

    private XSSFWorkbook libro;
    private XSSFSheet hoja;

    private List<EntidadBase> listaEntidades;


    public EntidadBaseExporterExcel(List<EntidadBase> listaEntidades) {
        this.listaEntidades = listaEntidades;
        libro = new XSSFWorkbook();
        hoja = libro.createSheet("Entidades");
    }


    private void escribirCabeceraDeLaTable(){
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
        celda.setCellValue("Razon Social");
        celda.setCellStyle(estilo);

        celda = fila.createCell(2);
        celda.setCellValue("CUIT");
        celda.setCellStyle(estilo);

        celda = fila.createCell(3);
        celda.setCellValue("Domicilio");
        celda.setCellStyle(estilo);

        celda = fila.createCell(4);
        celda.setCellValue("Telefono");
        celda.setCellStyle(estilo);

        celda = fila.createCell(5);
        celda.setCellValue("Correo");
        celda.setCellStyle(estilo);

    }

    private void escribirDatosDeLaTabla(){
        int numeroFilas = 1;
        CellStyle estilo = libro.createCellStyle();
        XSSFFont fuente = libro.createFont();
        fuente.setFontHeight(14);
        estilo.setFont(fuente);

        for (EntidadBase ent: listaEntidades) {
            Row fila = hoja.createRow(numeroFilas++);

            Cell celda = fila.createCell(0);
            celda.setCellValue(ent.getId());
            hoja.autoSizeColumn(0);
            celda.setCellStyle(estilo);


            celda = fila.createCell(1);
            celda.setCellValue(ent.getRazonSocial());
            hoja.autoSizeColumn(1);
            celda.setCellStyle(estilo);

            celda = fila.createCell(2);
            celda.setCellValue(ent.getCuit());
            hoja.autoSizeColumn(2);
            celda.setCellStyle(estilo);

            celda = fila.createCell(3);
            celda.setCellValue(ent.getDomicilio());
            hoja.autoSizeColumn(3);
            celda.setCellStyle(estilo);

            celda = fila.createCell(4);
            celda.setCellValue(ent.getTelefono());
            hoja.autoSizeColumn(4);
            celda.setCellStyle(estilo);

            celda = fila.createCell(5);
            celda.setCellValue(ent.getCorreo());
            hoja.autoSizeColumn(5);
            celda.setCellStyle(estilo);

        }

    }


    public void exportar(HttpServletResponse response) throws IOException {
        escribirCabeceraDeLaTable();
        escribirDatosDeLaTabla();

        ServletOutputStream outputStream = response.getOutputStream();
        libro.write(outputStream);
        libro.close();
        outputStream.close();

    }
}
