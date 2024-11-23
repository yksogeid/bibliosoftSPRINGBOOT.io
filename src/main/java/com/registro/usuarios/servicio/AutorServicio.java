package com.registro.usuarios.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.registro.usuarios.modelo.AutorModelo;
import com.registro.usuarios.repositorio.AutorRepositorio;

import java.util.List;
@Service
public class AutorServicio {

    @Autowired
    private AutorRepositorio autorRepositorio;

    // Listar todos los autores
    public List<AutorModelo> listarAutores() {
        return autorRepositorio.findAll();
    }

    // Guardar un autor (nuevo o actualizado)
    public void guardarAutor(AutorModelo autor) {
        autorRepositorio.save(autor);
    }

    // Obtener un autor por ID
    public AutorModelo obtenerAutorPorId(Long id) {
        return autorRepositorio.findById(id).orElse(null);
    }

    // Eliminar un autor por ID
    public void eliminarAutor(Long id) {
        autorRepositorio.deleteById(id);
    }
}
