package com.grupop.gestion.Reportes;

import com.grupop.gestion.Entidades.EntidadBase;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.io.IOException;
import java.util.List;

@NoArgsConstructor
public class EntidadBaseExporterPDF {

    private List<EntidadBase> listaEntidades;

    public EntidadBaseExporterPDF(List<EntidadBase> listaEntidades) {
        this.listaEntidades = listaEntidades;
    }

    private void escribirCabeceraDeLaTabla(PdfPTable tabla){
        //CREO LA FILA
        PdfPCell celda = new PdfPCell();
        celda.setBackgroundColor(Color.DARK_GRAY);
        celda.setPadding(5);
        Font fuente = FontFactory.getFont(FontFactory.HELVETICA);
        fuente.setColor(Color.WHITE);

        //CREO CADA CELDA / COLUMNA DEL ENCABEZADO
        celda.setPhrase(new Phrase("ID", fuente));
        tabla.addCell(celda);

        celda.setPhrase(new Phrase("Razon Social", fuente));
        tabla.addCell(celda);

        celda.setPhrase(new Phrase("CUIT", fuente));
        tabla.addCell(celda);

        celda.setPhrase(new Phrase("DOMICILIO", fuente));
        tabla.addCell(celda);

        celda.setPhrase(new Phrase("TELEFONO", fuente));
        tabla.addCell(celda);

        celda.setPhrase(new Phrase("CORREO", fuente));
        tabla.addCell(celda);
    }

    private void escribirDatosDeLaTabla( PdfPTable table){
//        Agrego los datos en el cuerpo de la tabla
        for (EntidadBase ent: listaEntidades) {
            table.addCell(ent.getId().toString());
            table.addCell(ent.getRazonSocial());
            table.addCell(ent.getCuit().toString());
            table.addCell(ent.getDomicilio());
            table.addCell(ent.getTelefono().toString());
            table.addCell(ent.getCorreo());

        }
    }

    public void exportar (HttpServletResponse response) throws IOException {
//        Creacion y configuracion del documento
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();
        Font fuente = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fuente.setColor(Color.BLACK);
        fuente.setSize(18);
        Paragraph titulo = new Paragraph("Lista de entidades", fuente);
        titulo.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(titulo);

        PdfPTable tabla = new PdfPTable(6);
        tabla.setWidthPercentage(100);
        tabla.setSpacingBefore(15);
        tabla.setWidths( new float[] {1f,3f,2f,3f,2f,3f});
        tabla.setWidthPercentage(110);

        escribirCabeceraDeLaTabla(tabla);
        escribirDatosDeLaTabla(tabla);
        document.add(tabla);
        document.close();

    }



}
