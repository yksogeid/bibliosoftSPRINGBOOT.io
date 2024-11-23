package com.registro.usuarios.modelo;

import javax.persistence.*;

@Entity
@Table(name = "libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255) // Define restricciones en la columna
    private String titulo;

    @ManyToOne(fetch = FetchType.LAZY) // Relación Many-to-One con carga perezosa
    @JoinColumn(name = "autor_id", nullable = false) // Clave foránea, no nula
    private AutorModelo autor;

    private Integer cantidad;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public AutorModelo getAutor() {
        return autor;
    }

    public void setAutor(AutorModelo autor) {
        this.autor = autor;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    
}
