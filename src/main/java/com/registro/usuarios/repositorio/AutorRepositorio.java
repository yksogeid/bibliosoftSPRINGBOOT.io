package com.registro.usuarios.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.registro.usuarios.modelo.AutorModelo;

@Repository
public interface AutorRepositorio extends JpaRepository<AutorModelo, Long> {
}

