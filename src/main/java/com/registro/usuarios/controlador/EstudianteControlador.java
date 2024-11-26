package com.registro.usuarios.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.registro.usuarios.modelo.Estudiante;
import com.registro.usuarios.servicio.EstudianteServicio;

import java.util.List;

@Controller
@RequestMapping("/estudiantes")
public class EstudianteControlador {

    @Autowired
    private EstudianteServicio estudianteServicio;

    @GetMapping
    public String listarEstudiantes(Model model) {
        List<Estudiante> estudiantes = estudianteServicio.listarEstudiantes();
        model.addAttribute("estudiantes", estudiantes);
        return "estudiantes";
    }

    @PostMapping("/crear")
    public String crearEstudiante(@ModelAttribute Estudiante estudiante) {
          PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String passwordEncriptada = passwordEncoder.encode(estudiante.getPassword());
            estudiante.setPassword(passwordEncriptada);
        estudianteServicio.guardarEstudiante(estudiante);
        return "redirect:/estudiantes";
    }

        // Método para actualizar un autor
    @PostMapping("/actualizar/{id}")
    public String estudiantesActualizar(@PathVariable Long id, @ModelAttribute Estudiante estudiante) {
        Estudiante estudianteExistente = estudianteServicio.obtenerEstudiantePorId(id);
        if (estudianteExistente != null) {
            estudianteExistente.setNombre(estudiante.getNombre());
            estudianteExistente.setApellido(estudiante.getApellido());
            estudianteExistente.setEmail(estudiante.getEmail());
            estudianteExistente.setTipodoc(estudiante.getTipodoc());
            estudianteExistente.setDocumento(estudiante.getDocumento());
            estudianteExistente.setNum1(estudiante.getNum1());
            estudianteExistente.setNum2(estudiante.getNum2());
            estudianteServicio.guardarEstudiante(estudianteExistente);
        }
        return "redirect:/estudiantes?exito=true";
    }

       @PostMapping("/eliminar")
    public String eliminarEstudiante(Long id) {
            estudianteServicio.eliminarEstudiante(id);
            return "redirect:/estudiantes?eliminado";
        }
}
