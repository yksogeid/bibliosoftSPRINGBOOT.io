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
            // Configurar el archivo PDF
            response.setContentType("application/pdf");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte_usuarios.pdf");

            document = new Document();
            out = response.getOutputStream();
            PdfWriter.getInstance(document, out);
            document.open();

            // Estilo general
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.WHITE);
            Font tableHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
            Font tableBodyFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);
            BaseColor primaryColor = new BaseColor(173, 216, 230); // Azul pastel claro
            BaseColor secondaryColor = new BaseColor(241, 241, 241); // Gris cálido para filas alternadas

            // Obtener la fecha y formatearla
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy HH:mm");
            String fecha = sdf.format(new Date());

            // Obtener el nombre del usuario que hizo la solicitud (Spring Security)
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            String usuarioImpresion = customUserDetails.getFullName(); // Aquí obtienes el nombre completo
            // Agregar encabezado con estilo
            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100);
            headerTable.setWidths(new int[] { 1, 3 }); // Ancho relativo de columnas
            headerTable.setSpacingAfter(20f); // Espaciado después del encabezado

            // Logo del proyecto
            String logoPath = "src/main/resources/static/imgs/DEFIS.PNG"; // Ruta del logo
            Image logo = Image.getInstance(logoPath);
            logo.scaleToFit(110, 100); // Ajustar tamaño del logo
            PdfPCell logoCell = new PdfPCell(logo);
            logoCell.setBorder(Rectangle.NO_BORDER);
            logoCell.setHorizontalAlignment(Element.ALIGN_LEFT); // Alineación izquierda
            logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE); // Centrado vertical
            headerTable.addCell(logoCell);

            // Texto adicional en el encabezado
            PdfPCell textCell = new PdfPCell(new Paragraph(
                    "Reporte de Usuarios\nBibliotrack - Gestión de bibliotecas\n" + fecha,
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE)));
            textCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            textCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            textCell.setBorder(Rectangle.NO_BORDER);
            textCell.setBackgroundColor(primaryColor); // Fondo verde
            textCell.setPadding(10f); // Espaciado interno
            headerTable.addCell(textCell);

            document.add(headerTable);

            // Línea divisoria
            document.add(new LineSeparator());

            // Espaciado
            document.add(new Paragraph(" "));

            // Agregar usuario que mandó a imprimir
            PdfPTable userTable = new PdfPTable(1);
            userTable.setWidthPercentage(100);
            PdfPCell userCell = new PdfPCell(new Paragraph(
                    "Reporte generado por: " + usuarioImpresion,
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.GRAY)));
            userCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            userCell.setBorder(Rectangle.NO_BORDER);
            userCell.setPadding(5f); // Espaciado interno
            userTable.addCell(userCell);

            document.add(userTable);

            // Obtener datos de la base de datos
            List<Usuario> usuarios = usuarioRepository.findAll();

            // Crear tabla con los datos
            PdfPTable table = new PdfPTable(3); // Número de columnas
            table.setWidthPercentage(100); // Ancho de la tabla
            table.setSpacingBefore(10f); // Espaciado antes de la tabla
            table.setSpacingAfter(10f); // Espaciado después de la tabla

            // Añadir encabezados de columna con estilo
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

            // Añadir filas de datos con alternancia de colores
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

                alternate = !alternate; // Alternar color
            }

            // Añadir la tabla al documento
            document.add(table);

            // Agregar pie de página con estilo
            document.add(new LineSeparator());
            PdfPTable footerTable = new PdfPTable(1);
            footerTable.setWidthPercentage(100);
            PdfPCell footerCell = new PdfPCell(new Paragraph(
                    "Contacto: yksogeid.dev@gmail.com | Tel: +57 3143480005\nCucuta, Norte de Santander",
                    FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.GRAY)));
            footerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            footerCell.setBorder(Rectangle.NO_BORDER);
            footerCell.setPadding(10f); // Espaciado interno
            footerTable.addCell(footerCell);

            document.add(footerTable);

        } catch (Exception e) {
            e.printStackTrace(); // Solo para desarrollo. En producción, usa un logger.
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
