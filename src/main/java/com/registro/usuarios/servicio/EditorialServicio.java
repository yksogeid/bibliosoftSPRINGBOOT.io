package com.registro.usuarios.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.registro.usuarios.modelo.Editorial;
import com.registro.usuarios.repositorio.EditorialRepositorio;

import java.util.List;
@Service
public class EditorialServicio {

    @Autowired
    private EditorialRepositorio editorialRepositorio;

    // Listar todos los autores
    public List<Editorial> listarEditorialeList() {
        return editorialRepositorio.findAll();
    }

    // Guardar un autor (nuevo o actualizado)
    public void guardarEditorial(Editorial editorial) {
        editorialRepositorio.save(editorial);
    }

    // Obtener un autor por ID
    public Editorial obtenerEditorialPorId(Long id) {
        return editorialRepositorio.findById(id).orElse(null);
    }

    // Eliminar un autor por ID
    public void eliminarEditorial(Long id) {
        editorialRepositorio.deleteById(id);
    }
}
