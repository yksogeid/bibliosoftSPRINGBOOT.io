package com.registro.usuarios.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.registro.usuarios.servicio.UsuarioServicio;

@Controller
public class VistaController {
    	@Autowired
	private UsuarioServicio servicio;

    @GetMapping("/")
    public String home() {
        return "inicio";
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    @GetMapping("/gestLibros")
    public String gestionarLibros() {
        return "gestLibros";
    }
    @GetMapping("/prestamoss")
    public String gestPrestamos() {
        return "prestamos2";
    }
    @GetMapping("/reportes")
    public String gestReportes() {
        return "reportes";
    }

    	@GetMapping("/usuarios")
	public String verPaginaDeInicio(Model modelo) {
		modelo.addAttribute("usuarios", servicio.listarUsuarios());
		return "listaUsuarios";
	}
}


