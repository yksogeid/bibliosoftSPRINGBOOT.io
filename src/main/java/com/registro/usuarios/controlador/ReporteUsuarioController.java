package com.registro.usuarios.controlador;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.registro.usuarios.modelo.CustomUserDetails;
import com.registro.usuarios.modelo.Usuario;
import com.registro.usuarios.repositorio.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class ReporteUsuarioController {

    @Autowired
    private UsuarioRepositorio usuarioRepository;

    @GetMapping("/reporte-usuarios")
    public void generarReporteUsuarios(HttpServletResponse response) {
        Document document = null;
        OutputStream out = null;

        try {
            response.setContentType("application/pdf");SimpleDateFormat sdfArchivo = new SimpleDateFormat("yyyyMMdd_HHmm");
            String fechaActual = sdfArchivo.format(new Date());

            String nombreArchivo = "reporteUsuarios_" + fechaActual + ".pdf";
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + nombreArchivo);

            document = new Document();
            out = response.getOutputStream();
            PdfWriter.getInstance(document, out);
            document.open();
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.WHITE);
            Font tableHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
            Font tableBodyFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);
            BaseColor primaryColor = new BaseColor(173, 216, 230);
            BaseColor secondaryColor = new BaseColor(241, 241, 241); 

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
                    "Reporte de Usuarios\nBibliotrack - Gestión de bibliotecas\n" + fecha,
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

            List<Usuario> usuarios = usuarioRepository.findAll();

            PdfPTable table = new PdfPTable(3); 
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f); 
            table.setSpacingAfter(10f);

            PdfPCell header1 = new PdfPCell(new Paragraph("Nombre", tableHeaderFont));
            header1.setBackgroundColor(primaryColor);
            header1.setHorizontalAlignment(Element.ALIGN_CENTER);
            header1.setPadding(5f);
            table.addCell(header1);

            PdfPCell header2 = new PdfPCell(new Paragraph("Apellido", tableHeaderFont));
            header2.setBackgroundColor(primaryColor);
            header2.setHorizontalAlignment(Element.ALIGN_CENTER);
            header2.setPadding(5f);
            table.addCell(header2);

            PdfPCell header3 = new PdfPCell(new Paragraph("Correo Electrónico", tableHeaderFont));
            header3.setBackgroundColor(primaryColor);
            header3.setHorizontalAlignment(Element.ALIGN_CENTER);
            header3.setPadding(5f);
            table.addCell(header3);

            boolean alternate = false;
            for (Usuario usuario : usuarios) {
                BaseColor rowColor = alternate ? secondaryColor : BaseColor.WHITE;

                PdfPCell cell1 = new PdfPCell(new Paragraph(
                        usuario.getNombre() != null ? String.valueOf(usuario.getNombre()) : "N/A", tableBodyFont));
                cell1.setBackgroundColor(rowColor);
                cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell1.setPadding(5f);
                table.addCell(cell1);

                PdfPCell cell2 = new PdfPCell(
                        new Paragraph(usuario.getApellido() != null ? usuario.getApellido() : "N/A", tableBodyFont));
                cell2.setBackgroundColor(rowColor);
                cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell2.setPadding(5f);
                table.addCell(cell2);

                PdfPCell cell3 = new PdfPCell(
                        new Paragraph(usuario.getEmail() != null ? usuario.getEmail() : "N/A", tableBodyFont));
                cell3.setBackgroundColor(rowColor);
                cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell3.setPadding(5f);
                table.addCell(cell3);

                alternate = !alternate;
            }
            document.add(table);
            document.add(new Paragraph(" "));
            document.add(new LineSeparator());
            PdfPTable footerTable = new PdfPTable(1);
            footerTable.setWidthPercentage(100);
            PdfPCell footerCell = new PdfPCell(new Paragraph(
                    "Contacto: yyksogeid.dev@hotmail.com | Tel: +57 3143480005\nCucuta, Norte de Santander",
                    FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.GRAY)));
            footerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            footerCell.setBorder(Rectangle.NO_BORDER);
            footerCell.setPadding(10f); 
            footerTable.addCell(footerCell);

            document.add(footerTable);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (document != null) {
                document.close();
            }
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace(); // Registrar errores en el cierre.
                }
            }
        }
    }
}
