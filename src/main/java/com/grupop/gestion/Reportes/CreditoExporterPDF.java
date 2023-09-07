package com.grupop.gestion.Reportes;

import com.grupop.gestion.Entidades.Credito;
import com.grupop.gestion.Entidades.CreditoDetalle;
import com.grupop.gestion.Entidades.EntidadBase;
import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.io.IOException;
import java.util.List;

@NoArgsConstructor
public class CreditoExporterPDF {

    private Credito credito;
    private List<CreditoDetalle> listaEntidades;

    public CreditoExporterPDF(Credito credito, List<CreditoDetalle> listaEntidades) {
        this.credito = credito;
        this.listaEntidades = listaEntidades;
    }

    private void escribirCabeceraDeLaTabla(PdfPTable tabla){


    }


}
