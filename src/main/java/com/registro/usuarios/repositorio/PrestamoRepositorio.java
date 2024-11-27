package com.registro.usuarios.repositorio;

import com.registro.usuarios.modelo.Prestamo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrestamoRepositorio extends JpaRepository<Prestamo, Long> {
}
