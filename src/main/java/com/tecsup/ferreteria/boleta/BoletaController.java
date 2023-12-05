package com.tecsup.ferreteria.boleta;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;

import lombok.RequiredArgsConstructor;

import java.io.ByteArrayOutputStream;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class BoletaController {
    private final BoletaService boletaService;

    @GetMapping("/generarPDF/{boletaId}")
    public ResponseEntity<byte[]> generarPDF(@PathVariable("boletaId") Long boletaId) {
        Boleta boleta = boletaService.getBoleta(boletaId);
        if (boleta != null) {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PdfWriter writer = new PdfWriter(baos);
                PdfDocument pdf = new PdfDocument(writer);
                Document document = new Document(pdf);
                document.add(new Paragraph("Boleta ID: " + boleta.getId()));
                Table table = new Table(UnitValue.createPercentArray(4)).useAllAvailableWidth();
                table.addCell("Nombre");
                table.addCell("Precio");
                table.addCell("Cantidad");
                table.addCell("Precio Total");
                for (ProductoDetalle detalle : boleta.getDetalles()) {
                    table.addCell(detalle.getNombre());
                    table.addCell(detalle.getPrecio().toString());
                    table.addCell(detalle.getCantidad().toString());
                    table.addCell(String.valueOf(detalle.getPrecio() * detalle.getCantidad()));
                }
                document.add(table);
                document.add(new Paragraph("Precio Total: " + boleta.getPrecioTotal()));
                document.close();
                byte[] pdfBytes = baos.toByteArray();
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_PDF)
                        .header("Content-Disposition", "inline; filename=boleta.pdf")
                        .contentLength(pdfBytes.length)
                        .body(pdfBytes);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
