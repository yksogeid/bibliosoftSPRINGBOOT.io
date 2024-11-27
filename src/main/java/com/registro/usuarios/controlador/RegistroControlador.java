package com.registro.usuarios.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.registro.usuarios.modelo.Estudiante;
import com.registro.usuarios.servicio.EstudianteServicio;

@Controller
@RequestMapping("/register")
public class RegistroControlador {

    @Autowired
    private EstudianteServicio estudianteServicio;
    @GetMapping("/student")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("estudiante", new Estudiante());
        return "crear-estudiante";
    }

    @PostMapping("/student")
    public String crearEstudiante(@ModelAttribute("estudiante") Estudiante estudiante, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "crear-estudiante";
        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String passwordEncriptada = passwordEncoder.encode(estudiante.getPassword());
        estudiante.setPassword(passwordEncriptada);
        estudianteServicio.guardarEstudiante(estudiante);
        model.addAttribute("mensaje", "Estudiante creado exitosamente");
        return "redirect:/login?exito";
    }
}