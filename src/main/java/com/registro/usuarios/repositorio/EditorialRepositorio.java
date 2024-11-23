package com.registro.usuarios.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.registro.usuarios.modelo.Editorial;

@Repository
public interface EditorialRepositorio extends JpaRepository<Editorial, Long> {
}

