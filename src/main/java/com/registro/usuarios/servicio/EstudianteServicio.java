package com.registro.usuarios.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.registro.usuarios.modelo.Estudiante;
import com.registro.usuarios.repositorio.EstudianteRepositorio;

import java.util.List;
@Service
public class EstudianteServicio {

    @Autowired
    private EstudianteRepositorio estudianteRepositorio;

    // Listar todos los autores
    public List<Estudiante> listarEstudiantes() {
        return estudianteRepositorio.findAll();
    }

    // Guardar un autor (nuevo o actualizado)
    public void guardarEstudiante(Estudiante estudiante) {
        estudianteRepositorio.save(estudiante);
    }

    // Obtener un autor por ID
    public Estudiante obtenerEstudiantePorId(Long id) {
        return estudianteRepositorio.findById(id).orElse(null);
    }

    // Eliminar un autor por ID
    public void eliminarEstudiante(Long id) {
        estudianteRepositorio.deleteById(id);
    }
}
