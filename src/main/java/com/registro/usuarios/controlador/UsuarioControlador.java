package com.registro.usuarios.controlador;

import com.registro.usuarios.modelo.Usuario;
import com.registro.usuarios.repositorio.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/usuarios")
public class UsuarioControlador {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    // Método para actualizar el usuario
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
        // Busca al usuario en la base de datos por su ID
        Optional<Usuario> usuarioOpt = usuarioRepositorio.findById(id);
        
        // Si el usuario existe
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            
            // Verifica si el usuario tiene el rol con ID 1
            boolean tieneRol1 = usuario.getRoles().stream()
                                .anyMatch(rol -> rol.getId() == 1);
    
            // Si el usuario tiene el rol 1, redirige con un mensaje de error
            if (tieneRol1) {
                return "redirect:/usuarios?prohibido";
            }
            
            // Si no tiene el rol 1, se procede a eliminar
            usuarioRepositorio.deleteById(id);
            return "redirect:/usuarios?eliminado";
        }
        
        // Redirige con mensaje de error si el usuario no existe
        return "redirect:/usuarios?error=noencontrado";
    }
    
    
}
