package com.ebac.modulo39.controller;

import com.ebac.modulo39.dto.Usuario;
import com.ebac.modulo39.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @GetMapping("/usuarios")
    public List<Usuario> obtenerUsuarios() {

        return usuarioService.obtenerUsuarios();
    }

    @GetMapping("/usuarios/{id}")
    public ResponseEntity<Usuario> obtenerUsuarioPorId(@PathVariable Long id) {
        Optional<Usuario> usuarioOptional = usuarioService.obtenerUsuarioPorId(id);

        return usuarioOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/usuarios")
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario usuario) throws URISyntaxException {
        usuarioService.crearUsuario(usuario);
        return ResponseEntity.created(new URI("http://localhost/usuarios")).build();
    }

    @PutMapping("/usuarios/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuarioActualizado) {
        Optional<Usuario> usuarioOptional = usuarioService.obtenerUsuarioPorId(id);

        if (usuarioOptional.isPresent()) {
            usuarioActualizado.setIdUsuario(usuarioOptional.get().getIdUsuario());
            usuarioService.actualizarUsuario(usuarioActualizado);

            return ResponseEntity.ok(usuarioActualizado);
        } else {
            return ResponseEntity.notFound(). build();
        }
    }

    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);

        return ResponseEntity.noContent().build();
    }
}
