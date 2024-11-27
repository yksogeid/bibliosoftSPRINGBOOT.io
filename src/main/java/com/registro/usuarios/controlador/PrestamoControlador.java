package com.registro.usuarios.controlador;
import com.registro.usuarios.modelo.Estudiante;
import com.registro.usuarios.modelo.Libro;
import com.registro.usuarios.modelo.Prestamo;
import com.registro.usuarios.servicio.PrestamoServicio;
import com.registro.usuarios.repositorio.EstudianteRepositorio;
import com.registro.usuarios.repositorio.LibroRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/prestamos")
public class PrestamoControlador {

    @Autowired
    private PrestamoServicio prestamoServicio;

    @Autowired
    private EstudianteRepositorio estudianteRepositorio;

    @Autowired
    private LibroRepositorio libroRepositorio;

    @GetMapping()
    public String listarPrestamos(Model modelo) {
        List<Prestamo> prestamos = prestamoServicio.listarTodos();
        List<Estudiante> estudiantes = estudianteRepositorio.findAll();
        List<Libro> libros = libroRepositorio.findAll();
        modelo.addAttribute("prestamos", prestamos);
        modelo.addAttribute("estudiantes", estudiantes);
        modelo.addAttribute("libros", libros);
        return "prestamos";
    }

    @PostMapping("/guardar")
    public String guardarPrestamo(Prestamo prestamo) {
        prestamoServicio.guardar(prestamo);
        return "redirect:/prestamos";
    }

        @PostMapping("/actualizar/{id}")
        public String actualizarPrestamo(@RequestParam("id") Long id, @ModelAttribute Prestamo prestamo) {
            Prestamo prestamoExiPrestamo = prestamoServicio.obtenerPorId(id);
            if (prestamoExiPrestamo != null) {
                prestamoExiPrestamo.setFechaInicial(prestamo.getFechaInicial());
                prestamoExiPrestamo.setFechaFinal(prestamo.getFechaFinal());

            // Si tienes que actualizar el autor y editorial, asegúrate de que esos valores también se proporcionen en el formulario
            if (prestamo.getEstudiante() != null) {
                prestamoExiPrestamo.setEstudiante(prestamo.getEstudiante());
            }
            if (prestamo.getLibro() != null) {
                prestamoExiPrestamo.setLibro(prestamo.getLibro());
            }
    
            // Guarda el libro actualizado
            prestamoServicio.guardar(prestamoExiPrestamo);;
              }
              return "redirect:/prestamos?exito=true";
        }
    



    @PostMapping("/eliminar")
    public String eliminarPrestamo(@RequestParam("id") Long id) {
        prestamoServicio.eliminar(id);
        return "redirect:/prestamos?eliminado";
    }
    
}
