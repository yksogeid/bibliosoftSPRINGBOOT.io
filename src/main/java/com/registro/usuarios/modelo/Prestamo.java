package com.registro.usuarios.modelo;

import javax.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "prestamos")
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


	@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "estudiante_id", nullable = false)
private Estudiante estudiante;

@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "libro_id", nullable = false)
	private Libro libro;	

	private Date fechaInicial;
	private Date fechaFinal;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Estudiante getEstudiante() {
		return estudiante;
	}
	public void setEstudiante(Estudiante estudiante) {
		this.estudiante = estudiante;
	}
	public Libro getLibro() {
		return libro;
	}
	public void setLibro(Libro libro) {
		this.libro = libro;
	}
	public Date getFechaInicial() {
		return fechaInicial;
	}
	public void setFechaInicial(Date fechaInicial) {
		this.fechaInicial = fechaInicial;
	}
	public Date getFechaFinal() {
		return fechaFinal;
	}
	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
	}
	public Prestamo(Long id, Estudiante estudiante, Libro libro, Date fechaInicial, Date fechaFinal) {
		this.id = id;
		this.estudiante = estudiante;
		this.libro = libro;
		this.fechaInicial = fechaInicial;
		this.fechaFinal = fechaFinal;
	}
	public Prestamo() {
	}

	
}
