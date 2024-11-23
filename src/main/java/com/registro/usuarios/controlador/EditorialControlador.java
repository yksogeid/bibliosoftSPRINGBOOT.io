package com.registro.usuarios.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.registro.usuarios.modelo.Editorial;
import com.registro.usuarios.servicio.EditorialServicio;

import java.util.List;

@Controller
@RequestMapping("/editoriales")
public class EditorialControlador {

    @Autowired
    private EditorialServicio editorialServicio;

    @GetMapping
    public String listarEditoriales(Model model) {
        List<Editorial> editoriales = editorialServicio.listarEditorialeList();
        model.addAttribute("editoriales", editoriales);
        return "editoriales";
    }

    @PostMapping("/crear")
    public String crearEditorial(@ModelAttribute Editorial editorial) {
        editorialServicio.guardarEditorial(editorial);
        return "redirect:/editoriales";
    }

    // Método para actualizar un autor
    @PostMapping("/actualizar/{id}")
    public String actualizarEditorial(@PathVariable Long id, @ModelAttribute Editorial editorial) {
        Editorial editorialExistente = editorialServicio.obtenerEditorialPorId(id);
        if (editorialExistente != null) {
            editorialExistente.setNombre(editorial.getNombre());
            editorialExistente.setPais(editorial.getPais());
            editorialExistente.setWeb(editorial.getWeb());
            editorialExistente.setCiudad(editorial.getCiudad());
            editorialServicio.guardarEditorial(editorialExistente);
        }
        return "redirect:/editoriales?exito=true";
    }

    // Método para eliminar un autor
    @PostMapping("/eliminar/{id}")
    public String eliminarEditorial(@PathVariable Long id) {
        try {
            editorialServicio.eliminarEditorial(id);
            return "redirect:/editoriales?eliminado=true";
        } catch (Exception e) {
            // Manejo de error al eliminar
            return "redirect:/editoriales?error=noencontrado";
        }
    }
}
