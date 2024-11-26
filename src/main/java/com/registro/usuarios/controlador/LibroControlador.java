package com.registro.usuarios.controlador;

import com.registro.usuarios.modelo.AutorModelo;
import com.registro.usuarios.modelo.Editorial;
import com.registro.usuarios.modelo.Libro;
import com.registro.usuarios.servicio.LibroServicio;
import com.registro.usuarios.repositorio.EditorialRepositorio;
import com.registro.usuarios.repositorio.AutorRepositorio;
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
@RequestMapping("/libros")
public class LibroControlador {

    @Autowired
    private LibroServicio libroServicio;

    @Autowired
    private EditorialRepositorio editorialRepositorio;

    @Autowired
    private AutorRepositorio autorRepositorio;

    @GetMapping()
    public String listarLibros(Model modelo) {
        List<Libro> libros = libroServicio.listarTodos();
        List<Editorial> editoriales = editorialRepositorio.findAll();
        List<AutorModelo> autores = autorRepositorio.findAll();
        modelo.addAttribute("libros", libros);
        modelo.addAttribute("editoriales", editoriales);
        modelo.addAttribute("autores", autores);
        return "libros";
    }

    @PostMapping("/guardar")
    public String guardarLibro(Libro libro) {
        libroServicio.guardar(libro);
        return "redirect:/libros";
    }

        @PostMapping("/actualizar/{id}")
        public String actualizarLibro(@RequestParam("id") Long id, @ModelAttribute Libro libro) {
            Libro libroExistente = libroServicio.obtenerPorId(id);
            if (libroExistente != null) {
                          // Actualiza los campos del libro
            libroExistente.setTitulo(libro.getTitulo());
            libroExistente.setIsbn(libro.getIsbn());
            libroExistente.setAnioPublicacion(libro.getAnioPublicacion());
            libroExistente.setPrecio(libro.getPrecio());
            
            // Si tienes que actualizar el autor y editorial, asegúrate de que esos valores también se proporcionen en el formulario
            if (libro.getAutor() != null) {
                libroExistente.setAutor(libro.getAutor());
            }
            if (libro.getEditorial() != null) {
                libroExistente.setEditorial(libro.getEditorial());
            }
    
            // Guarda el libro actualizado
            libroServicio.guardar(libroExistente);;
              }
              return "redirect:/libros?exito=true";
        }
    



    @PostMapping("/eliminar")
    public String eliminarLibro(@RequestParam("id") Long id) {
        libroServicio.eliminar(id);
        return "redirect:/libros?eliminado";
    }
    
}
