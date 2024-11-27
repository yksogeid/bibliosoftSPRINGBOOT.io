package com.registro.usuarios.controlador;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.registro.usuarios.modelo.CustomUserDetails;
import com.registro.usuarios.modelo.Libro;
import com.registro.usuarios.repositorio.LibroRepositorio;
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
public class ReporteLibrosController {

    @Autowired
    private LibroRepositorio libroRepository;

    @GetMapping("/reporte-libros")
    public void generarReporteLibros(HttpServletResponse response) {
        Document document = null;
        OutputStream out = null;

        try {
            response.setContentType("application/pdf");
            SimpleDateFormat sdfArchivo = new SimpleDateFormat("yyyyMMdd_HHmm");
            String fechaActual = sdfArchivo.format(new Date());

            String nombreArchivo = "reporteLibros_" + fechaActual + ".pdf";
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + nombreArchivo);

            document = new Document();
            out = response.getOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.open();
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.WHITE);
            Font tableHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
            Font tableBodyFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);
            BaseColor primaryColor = new BaseColor(173, 216, 230);
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy HH:mm");
            String fecha = sdf.format(new Date());
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            String usuarioImpresion = customUserDetails.getFullName();
            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100);
            headerTable.setWidths(new int[] { 1, 3 });
            headerTable.setSpacingAfter(20f);
            String logoPath = "src/main/resources/static/imgs/DEFIS.PNG";
            Image logo = Image.getInstance(logoPath);
            logo.scaleToFit(110, 100);
            PdfPCell logoCell = new PdfPCell(logo);
            logoCell.setBorder(Rectangle.NO_BORDER);
            logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            headerTable.addCell(logoCell);
            PdfPCell textCell = new PdfPCell(new Paragraph(
                    "Reporte de Libros\nBibliotrack - Gestión de bibliotecas\n" + fecha,
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE)));
            textCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            textCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            textCell.setBorder(Rectangle.NO_BORDER);
            textCell.setBackgroundColor(primaryColor);
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
            userCell.setPadding(5f);
            userTable.addCell(userCell);

            document.add(userTable);
            List<Libro> libros = libroRepository.findAll();
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            String[] headers = { "Titulo", "Isbn", "Publicacion", "Autor", "Editorial" };
            for (String header : headers) {
                PdfPCell headerCell = new PdfPCell(new Paragraph(header, tableHeaderFont));
                headerCell.setBackgroundColor(primaryColor);
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerCell.setPadding(5f);
                table.addCell(headerCell);
            }

            SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
            for (Libro libro : libros) {
                PdfPCell cell1 = new PdfPCell(new Paragraph(libro.getTitulo(), tableBodyFont));
                cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell1.setPadding(5f);
                table.addCell(cell1);

                PdfPCell cell2 = new PdfPCell(new Paragraph(libro.getIsbn(), tableBodyFont));
                cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell2.setPadding(5f);
                table.addCell(cell2);

                Integer anioPublicacion = libro.getAnioPublicacion();
                String anioPublicacionStr = (anioPublicacion != null) ? anioPublicacion.toString() : "N/A"; // Si es
                                                                                                            // null,
                                                                                                            // mostrar
                                                                                                            // "N/A"
                PdfPCell cell3 = new PdfPCell(new Paragraph(anioPublicacionStr, tableBodyFont));
                cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell3.setPadding(5f);
                table.addCell(cell3);

                PdfPCell cell4 = new PdfPCell(new Paragraph(libro.getAutor().getNombre(), tableBodyFont));
                cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell4.setPadding(5f);
                table.addCell(cell4);

                PdfPCell cell5 = new PdfPCell(new Paragraph(libro.getEditorial().getNombre(), tableBodyFont));
                cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell5.setPadding(5f);
                table.addCell(cell5);
            }

            document.add(table);

            float pageHeight = document.getPageSize().getHeight();
            float footerMargin = 30f;
            float contentHeight = table.getTotalHeight();

            if (contentHeight + footerMargin > pageHeight) {
                document.newPage();
            }

            PdfPTable footerTable = new PdfPTable(1);
            footerTable.setWidthPercentage(100);
            PdfPCell footerCell = new PdfPCell(new Paragraph(
                    "Contacto: yksogeid.dev@hotmail.com | Tel: +57 3143480005\nCucuta, Norte de Santander",
                    FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.GRAY)));
            footerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            footerCell.setBorder(Rectangle.NO_BORDER);
            footerCell.setPadding(10f);
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
