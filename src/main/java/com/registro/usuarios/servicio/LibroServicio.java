package com.registro.usuarios.servicio;

import com.registro.usuarios.modelo.Libro;
import com.registro.usuarios.repositorio.LibroRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibroServicio {

    @Autowired
    private LibroRepositorio libroRepositorio;

    public List<Libro> listarTodos() {
        return libroRepositorio.findAll();
    }

    public void guardar(Libro libro) {
        libroRepositorio.save(libro);
    }

    public Libro obtenerPorId(Long id) {
        return libroRepositorio.findById(id).orElse(null);
    }

    public void eliminar(Long id) {
        libroRepositorio.deleteById(id);
    }
}

