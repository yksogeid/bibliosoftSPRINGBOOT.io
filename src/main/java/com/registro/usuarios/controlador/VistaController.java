package com.registro.usuarios.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VistaController {
    @GetMapping("/")
    public String home() {
        return "inicio";
    }
    @GetMapping("/gestLibros")
    public String gestionarLibros() {
        return "gestLibros";
    }
    @GetMapping("/prestamos")
    public String gestPrestamos() {
        return "prestamos";
    }
    @GetMapping("/reportes")
    public String gestReportes() {
        return "reportes";
    }
}


