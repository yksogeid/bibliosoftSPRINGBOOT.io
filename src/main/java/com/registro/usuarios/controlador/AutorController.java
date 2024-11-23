package com.registro.usuarios.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.registro.usuarios.modelo.AutorModelo;
import com.registro.usuarios.servicio.AutorServicio;

import java.util.List;

@Controller
@RequestMapping("/autores")
public class AutorController {

    @Autowired
    private AutorServicio autorServicio;

    @GetMapping
    public String listarAutores(Model model) {
        List<AutorModelo> autores = autorServicio.listarAutores();
        model.addAttribute("autores", autores);
        return "autores";
    }

    @PostMapping("/crear")
    public String crearAutor(@ModelAttribute AutorModelo autor) {
        autorServicio.guardarAutor(autor);
        return "redirect:/autores";
    }

    // Método para actualizar un autor
    @PostMapping("/actualizar/{id}")
    public String actualizarAutor(@PathVariable Long id, @ModelAttribute AutorModelo autor) {
        AutorModelo autorExistente = autorServicio.obtenerAutorPorId(id);
        if (autorExistente != null) {
            autorExistente.setNombre(autor.getNombre());
            autorExistente.setApellido(autor.getApellido());
            autorExistente.setBiografia(autor.getBiografia());
            autorServicio.guardarAutor(autorExistente);
        }
        return "redirect:/autores?exito=true";
    }

    // Método para eliminar un autor
    @PostMapping("/eliminar/{id}")
    public String eliminarAutor(@PathVariable Long id) {
        try {
            autorServicio.eliminarAutor(id);
            return "redirect:/autores?eliminado=true";
        } catch (Exception e) {
            // Manejo de error al eliminar
            return "redirect:/autores?error=noencontrado";
        }
    }
}
