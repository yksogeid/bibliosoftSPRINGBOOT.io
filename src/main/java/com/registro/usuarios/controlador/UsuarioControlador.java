package com.registro.usuarios.controlador;

import com.registro.usuarios.modelo.Usuario;
import com.registro.usuarios.repositorio.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Controller
@RequestMapping("/usuarios")
public class UsuarioControlador {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @PostMapping("/actualizar")
    public String actualizarUsuario(Usuario usuario) {
        Optional<Usuario> usuarioExistente = usuarioRepositorio.findById(usuario.getId());
        
        if (usuarioExistente.isPresent()) {
            Usuario usuarioActualizado = usuarioExistente.get();
            usuarioActualizado.setNombre(usuario.getNombre());
            usuarioActualizado.setApellido(usuario.getApellido());
            usuarioActualizado.setEmail(usuario.getEmail());
            // Guardar cambios en la base de datos
            usuarioRepositorio.save(usuarioActualizado);
        }
        return "redirect:/usuarios?exito";
    }

    @PostMapping("/eliminar")
    public String eliminarUsuario(Long id) {
        Optional<Usuario> usuarioOpt = usuarioRepositorio.findById(id);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            
            boolean tieneRolAdmin = usuario.getRoles().stream()
                                          .anyMatch(rol -> rol.getNombre().equals("ROLE_ADMIN"));
            
            if (tieneRolAdmin) {
                return "redirect:/usuarios?prohibido";
            }
            
            usuario.getRoles().clear();  
            usuarioRepositorio.save(usuario);  
            usuarioRepositorio.deleteById(id);
            return "redirect:/usuarios?eliminado";
        }
        return "redirect:/usuarios?no_encontrado";
    }
    
    @PostMapping("/crear")
    public String crearUsuario(Usuario usuario) {
        Optional<Usuario> usuarioExistente = Optional.ofNullable(usuarioRepositorio.findByEmail(usuario.getEmail()));
    
        if (usuarioExistente.isPresent()) {
            return "redirect:/usuarios?error=existe";
        }

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String passwordEncriptada = passwordEncoder.encode(usuario.getPassword());
            usuario.setPassword(passwordEncriptada);
            usuarioRepositorio.save(usuario);
        return "redirect:/usuarios?creado";
    }
    
    
}
