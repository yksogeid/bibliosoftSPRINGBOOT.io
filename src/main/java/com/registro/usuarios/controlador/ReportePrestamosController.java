package com.registro.usuarios.controlador;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.registro.usuarios.modelo.CustomUserDetails;
import com.registro.usuarios.modelo.Prestamo;
import com.registro.usuarios.repositorio.PrestamoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class ReportePrestamosController {

    @Autowired
    private PrestamoRepositorio prestamoRepository;

    @GetMapping("/reporte-prestamos")
    public void generarReportePrestamos(HttpServletResponse response) {
        Document document = null;
        OutputStream out = null;

        try {
            response.setContentType("application/pdf");
            SimpleDateFormat sdfArchivo = new SimpleDateFormat("yyyyMMdd_HHmm");
            String fechaActual = sdfArchivo.format(new Date());

            String nombreArchivo = "reportePrestamos_" + fechaActual + ".pdf";
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + nombreArchivo);

            document = new Document();
            out = response.getOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.open();

            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.WHITE);
            Font tableHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
            Font tableBodyFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);
            BaseColor primaryColor = new BaseColor(173, 216, 230); // Azul pastel claro

            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy HH:mm");
            String fecha = sdf.format(new Date());

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            String usuarioImpresion = customUserDetails.getFullName();

            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100);
            headerTable.setWidths(new int[]{1, 3});

            String logoPath = "src/main/resources/static/imgs/DEFIS.PNG"; // Ruta del logo
            Image logo = Image.getInstance(logoPath);
            logo.scaleToFit(110, 100);
            PdfPCell logoCell = new PdfPCell(logo);
            logoCell.setBorder(Rectangle.NO_BORDER);
            headerTable.addCell(logoCell);

            PdfPCell textCell = new PdfPCell(new Paragraph(
                    "Reporte de Préstamos\nBibliotrack - Gestión de Bibliotecas\n" + fecha,
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE)));
            textCell.setBackgroundColor(primaryColor);
            textCell.setBorder(Rectangle.NO_BORDER);
            textCell.setPadding(10f);
            headerTable.addCell(textCell);

            document.add(headerTable);
            document.add(new LineSeparator());
            document.add(new Paragraph(" "));

            PdfPTable userTable = new PdfPTable(1);
            userTable.setWidthPercentage(100);
            PdfPCell userCell = new PdfPCell(new Paragraph(
                    "Reporte generado por: " + usuarioImpresion,
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.GRAY)));
            userCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            userCell.setBorder(Rectangle.NO_BORDER);
            userTable.addCell(userCell);

            document.add(userTable);

            List<Prestamo> prestamos = prestamoRepository.findAll();

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            String[] headers = {"Titulo del Libro", "Estudiante", "Fecha Préstamo", "Fecha para Devolución", "Estado"};
            for (String header : headers) {
                PdfPCell headerCell = new PdfPCell(new Paragraph(header, tableHeaderFont));
                headerCell.setBackgroundColor(primaryColor);
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerCell.setPadding(5f);
                table.addCell(headerCell);
            }

            for (Prestamo prestamo : prestamos) {
                PdfPCell cell1 = new PdfPCell(new Paragraph(prestamo.getLibro().getTitulo(), tableBodyFont));
                cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell1.setPadding(5f);
                table.addCell(cell1);

                PdfPCell cell2 = new PdfPCell(new Paragraph(prestamo.getEstudiante().getNombre(), tableBodyFont));
                cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell2.setPadding(5f);
                table.addCell(cell2);

                PdfPCell cell3 = new PdfPCell(new Paragraph(
    sdf.format(prestamo.getFechaInicial()), tableBodyFont));
cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
cell3.setPadding(5f);
table.addCell(cell3);


                String fechaDevolucion = prestamo.getFechaFinal() != null
                        ? sdf.format(prestamo.getFechaFinal())
                        : "Pendiente";
                PdfPCell cell4 = new PdfPCell(new Paragraph(fechaDevolucion, tableBodyFont));
                cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell4.setPadding(5f);
                table.addCell(cell4);

                PdfPCell cell5 = new PdfPCell(new Paragraph("PRESTADO", tableBodyFont));
                cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell5.setPadding(5f);
                table.addCell(cell5);
            }

            document.add(table);
            document.add(new Paragraph(" "));

            PdfPTable footerTable = new PdfPTable(1);
            footerTable.setWidthPercentage(100);
            PdfPCell footerCell = new PdfPCell(new Paragraph(
                    "Contacto: yksogeid.dev@hotmail.com | Tel: +57 3143480005\nCúcuta, Norte de Santander",
                    FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.GRAY)));
            footerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            footerCell.setBorder(Rectangle.NO_BORDER);
            footerTable.addCell(footerCell);

            document.add(new LineSeparator());
            document.add(footerTable);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (document != null) {
                document.close();
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
