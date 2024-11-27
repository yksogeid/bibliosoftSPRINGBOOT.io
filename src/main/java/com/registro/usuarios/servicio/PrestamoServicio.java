package com.registro.usuarios.servicio;

import com.registro.usuarios.modelo.Prestamo;
import com.registro.usuarios.repositorio.PrestamoRepositorio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrestamoServicio {

    @Autowired
    private PrestamoRepositorio prestamoRepositorio;

    public List<Prestamo> listarTodos() {
        return prestamoRepositorio.findAll();
    }

    public void guardar(Prestamo Prestamo) {
        prestamoRepositorio.save(Prestamo);
    }

    public Prestamo obtenerPorId(Long id) {
        return prestamoRepositorio.findById(id).orElse(null);
    }

    public void eliminar(Long id) {
        prestamoRepositorio.deleteById(id);
    }
}

