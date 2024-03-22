package com.ebac.modulo39.controller;

import com.ebac.modulo39.dto.Usuario;
import com.ebac.modulo39.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    @Mock
    UsuarioService usuarioService;

    @InjectMocks
    UsuarioController usuarioController;

    @Test
    void obtenerUsuarios() {
        int usuarios = 5;
        List<Usuario> usuariosListExpected = crearUsuarios(usuarios);

        //Comportamiento del Mock
        when(usuarioService.obtenerUsuarios()).thenReturn(usuariosListExpected);

        //Se ejecuta el metodo del controlador
        List<Usuario> usuariosListActual = usuarioController.obtenerUsuarios();

        //Validacion de resultado
        assertEquals(usuarios, usuariosListActual.size());
        assertEquals(usuariosListExpected, usuariosListActual);
    }

    @Test
    void ObtenerUsuariosCuandoNoExisten() {
        //Comportamiento del Mock
        when(usuarioService.obtenerUsuarios()).thenReturn(List.of());

        //Se ejecuta el metodo del contorlador
        List<Usuario> usuarioListActual = usuarioController.obtenerUsuarios();

        //Se valida resultado
        assertTrue(usuarioListActual.isEmpty());

        verify(usuarioService, times(1)).obtenerUsuarios();
    }

    @Test
    void obtenerUsuarioPorId() {
        long idUsuario = 1;
        Optional<Usuario> usuarioExpected = Optional.of(crearUsuarios(1).get(0));

        //Comportamiento del Mock
        when(usuarioService.obtenerUsuarioPorId(idUsuario)).thenReturn(usuarioExpected);

        //Se ejecuta el metodo del controlador
        ResponseEntity<Usuario> usuarioResponseEntity = usuarioController.obtenerUsuarioPorId(idUsuario);
        Usuario usuarioActual = usuarioResponseEntity.getBody();

        //Se valida el resultado
        assertEquals(200, usuarioResponseEntity.getStatusCode().value());
        assertNotNull(usuarioActual);
        assertEquals("Usuario1", usuarioActual.getNombre());
    }

    @Test
    void obtenerUsuarioPorIdCuandoNoExiste() {
        long idUsuario = 1;

        //Se configura el comportamiento del Mock
        when(usuarioService.obtenerUsuarioPorId(idUsuario)).thenReturn(Optional.empty());

        //Se ejecuta el metodo del controlador
        ResponseEntity<Usuario> usuarioResponseEntity = usuarioController.obtenerUsuarioPorId(idUsuario);
        Usuario usuarioActual = usuarioResponseEntity.getBody();

        //Se valida el resultado
        assertEquals(404, usuarioResponseEntity.getStatusCode().value());
        assertTrue(Objects.isNull(usuarioActual));
    }

    @Test
    void crearUsuario() throws URISyntaxException {
        Usuario usuarioExpected = crearUsuarios(1).get(0);

        //Configuracion del comportamiento del Mock
        when(usuarioService.crearUsuario(usuarioExpected)).thenReturn(usuarioExpected);

        //Se el metodo del controlador
        ResponseEntity<Usuario> usuarioResponseEntity = usuarioController.crearUsuario(usuarioExpected);
        Usuario usuarioActual = usuarioResponseEntity.getBody();

        //Se valida el resultado
        assertEquals(201, usuarioResponseEntity.getStatusCode().value());
        assertTrue(Objects.isNull(usuarioActual));
    }

    @Test
    void actualizarUsuario() {
        int idUsuario = 3;
        String nombreActualizado = "Evelyn";
        int edadActualizada = 35;

        Usuario usuarioPasado = new Usuario();
        usuarioPasado.setIdUsuario(idUsuario);
        usuarioPasado.setNombre("Fernanda");
        usuarioPasado.setEdad(25);

        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setNombre(nombreActualizado);
        usuarioActualizado.setEdad(edadActualizada);

        //Configuracion comportamiento del Mock
        when(usuarioService.obtenerUsuarioPorId((long) idUsuario)).thenReturn(Optional.of(usuarioPasado));
        doNothing().when(usuarioService).actualizarUsuario(usuarioActualizado);

        //Se ejecuta el metodo del controlador
        ResponseEntity<Usuario> usuarioResponseEntity = usuarioController.actualizarUsuario((long) idUsuario, usuarioActualizado);
        Usuario usuarioActual = usuarioResponseEntity.getBody();

        //Se valida el resultado
        assertEquals(200, usuarioResponseEntity.getStatusCode().value());
        assertNotNull(usuarioActual);
        assertEquals(idUsuario, usuarioActual.getIdUsuario());
        assertEquals(nombreActualizado, usuarioActual.getNombre());
        assertEquals(edadActualizada, usuarioActual.getEdad());
    }

    @Test
    void actualizarUsuarioCuandoNoexiste() {
        long idUsuario = 3;
        String nombreActualizado = "Evelyn";
        int edadActualizada = 35;

        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setNombre(nombreActualizado);
        usuarioActualizado.setEdad(edadActualizada);

        //Configuracion comportamiento del Mock
        when(usuarioService.obtenerUsuarioPorId(idUsuario)).thenReturn(Optional.empty());

        //Se ejecuta el metodo del controlador
        ResponseEntity<Usuario> usuarioResponseEntity = usuarioController.actualizarUsuario((long) idUsuario, usuarioActualizado);
        Usuario usuarioActual = usuarioResponseEntity.getBody();

        //Se valida el resultado
        assertEquals(404, usuarioResponseEntity.getStatusCode().value());
        assertNull(usuarioActual);
        verify(usuarioService, never()).actualizarUsuario(usuarioActualizado);
    }

    @Test
    void eliminarUsuario() {
        long idUsuario = 1;
        //Configuracion comportamiento del Mock
        doNothing().when(usuarioService).eliminarUsuario(idUsuario);

        //Se ejecuta el metodo del controlador
        ResponseEntity<Void> responseEntity = usuarioController.eliminarUsuario(idUsuario);

        //Se valida el resultado
        assertEquals(204, responseEntity.getStatusCode().value());
        verify(usuarioService, times(1)).eliminarUsuario(idUsuario);
    }

    private List<Usuario> crearUsuarios(int elementos) {
        return IntStream.range(1, elementos + 1).mapToObj(i -> {
            Usuario usuario = new Usuario();
            usuario.setIdUsuario(i);
            usuario.setNombre("Usuario" + i);
            usuario.setEdad(20 + i);
            return usuario;
        }).collect(Collectors.toList());
    }
}