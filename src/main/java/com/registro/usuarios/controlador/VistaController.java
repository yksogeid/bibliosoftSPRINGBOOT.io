package com.registro.usuarios.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VistaController {
    @GetMapping("/")
    public String home() {
        // Redirige a la vista "usuarios.html"
        return "index";
    }
    @GetMapping("/libros")
    public String gestionarLibros() {
        // Redirige a la vista "libros.html"
        return "gestLibros";
    }
}

