package com.registro.usuarios.servicio;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.registro.usuarios.controlador.dto.UsuarioRegistroDTO;
import com.registro.usuarios.modelo.CustomUserDetails;
import com.registro.usuarios.modelo.Rol;
import com.registro.usuarios.modelo.Usuario;
import com.registro.usuarios.repositorio.UsuarioRepositorio;

@Service
public class UsuarioServicioImpl implements UsuarioServicio {

	
	private UsuarioRepositorio usuarioRepositorio;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	public UsuarioServicioImpl(UsuarioRepositorio usuarioRepositorio) {
		super();
		this.usuarioRepositorio = usuarioRepositorio;
	}

	@Override
	public Usuario guardar(UsuarioRegistroDTO registroDTO) {
		Usuario usuario = new Usuario(registroDTO.getNombre(), 
				registroDTO.getApellido(),registroDTO.getEmail(),
				passwordEncoder.encode(registroDTO.getPassword()),Arrays.asList(new Rol("ROLE_USER")));
		return usuarioRepositorio.save(usuario);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// Cargar el usuario desde la base de datos usando el email como nombre de usuario
		Usuario usuario = usuarioRepositorio.findByEmail(username);
		
		if (usuario == null) {
			throw new UsernameNotFoundException("Usuario o contraseña incorrectos");
		}
	
		// Concatenar nombre y apellido
		String fullName = usuario.getNombre() + " " + usuario.getApellido();
		
	
		// Mapear roles a autoridades (authorities)
		List<GrantedAuthority> authorities = (List<GrantedAuthority>) mapearAutoridadesRoles(usuario.getRoles());

		String rol = authorities.stream()
                            .map(GrantedAuthority::getAuthority)
                            .findFirst() // Obtener el primer rol (asumiendo que el usuario tiene un rol asignado)
                            .orElse("SIN_ROL)");
	
		// Imprimir en consola los datos
		System.out.println("Email: " + usuario.getEmail());
		System.out.println("Nombre completo: " + fullName);
		System.out.println("Roles: " + authorities.stream()
			.map(GrantedAuthority::getAuthority) // Extraer solo el nombre del rol
			.sorted() // Ordenar los roles alfabéticamente
			.collect(Collectors.joining(", ")));
			System.out.println("Rol asignado: " + rol); // Imprimir el rol

	
		// Retornar el usuario con nombre completo y sus roles (authorities)
		return new CustomUserDetails(usuario.getEmail(), usuario.getPassword(), authorities, fullName, rol);
	}
	

	private Collection<? extends GrantedAuthority> mapearAutoridadesRoles(Collection<Rol> roles){
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getNombre())).collect(Collectors.toList());
	}
	
	@Override
	public List<Usuario> listarUsuarios() {
		return usuarioRepositorio.findAll();
	}
}
