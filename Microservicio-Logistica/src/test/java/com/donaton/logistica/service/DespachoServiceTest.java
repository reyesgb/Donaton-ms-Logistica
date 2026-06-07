package com.donaton.logistica.service;

import com.donaton.logistica.dto.DespachoDTO;
import com.donaton.logistica.model.Envio;
import com.donaton.logistica.model.Recurso;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DespachoServiceTest {

    @Mock
    private RecursoService recursoService;

    @Mock
    private EnvioService envioService;

    @Mock
    private NecesidadClientService necesidadClient;

    @InjectMocks
    private DespachoService despachoService;

    private DespachoDTO despachoDTO;
    private Recurso recurso;

    @BeforeEach
    void setUp() {
        // Inicializamos los objetos base antes de cada prueba
        despachoDTO = new DespachoDTO();
        despachoDTO.setCategoria(" aGuA "); // Agregamos espacios y minúsculas para probar el .toUpperCase().trim()
        despachoDTO.setCantidad(10);
        despachoDTO.setDestino("Sede Central");
        despachoDTO.setNecesidadId(100L);

        recurso = new Recurso();
        recurso.setCantidadDisponible(20); // Stock suficiente por defecto
    }

    @Test
    void despachar_DebeCrearEnvioYNotificar_CuandoTodoEsCorrecto() {
        // Arrange
        when(recursoService.buscarPorCategoria("AGUA")).thenReturn(recurso);
        when(envioService.guardar(any(Envio.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Envio resultado = despachoService.despachar(despachoDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals("AGUA", resultado.getCategoria());
        assertEquals(10, resultado.getCantidadDespachada());
        assertEquals(100L, resultado.getNecesidadId());
        assertEquals("Sede Central", resultado.getDestino());
        assertEquals(LocalDate.now(), resultado.getFecha());

        // Verificamos que se descontó el stock y se llamó al cliente
        verify(recursoService, times(1)).descontarStock("AGUA", 10);
        verify(necesidadClient, times(1)).completarNecesidad(100L);
    }

    @Test
    void despachar_DebeCrearEnvioSinNotificar_CuandoNecesidadIdEsNull() {
        // Arrange
        despachoDTO.setNecesidadId(null); // Modificamos para entrar al caso sin necesidadId
        when(recursoService.buscarPorCategoria("AGUA")).thenReturn(recurso);
        when(envioService.guardar(any(Envio.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Envio resultado = despachoService.despachar(despachoDTO);

        // Assert
        assertNotNull(resultado);
        assertNull(resultado.getNecesidadId());

        // Verificamos que NO se llamó al cliente
        verify(necesidadClient, never()).completarNecesidad(any());
    }

    @Test
    void despachar_DebeLanzarExcepcion_CuandoNoExisteStockParaCategoria() {
        // Arrange
        when(recursoService.buscarPorCategoria("AGUA")).thenReturn(null);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            despachoService.despachar(despachoDTO);
        });

        assertEquals("No existe stock para la categoría: AGUA", exception.getMessage());
        // Verificamos que el proceso se detuvo y no se guardó nada
        verify(envioService, never()).guardar(any());
    }

    @Test
    void despachar_DebeLanzarExcepcion_CuandoStockEsInsuficiente() {
        // Arrange
        recurso.setCantidadDisponible(5); // Solo hay 5, pero pedimos 10
        when(recursoService.buscarPorCategoria("AGUA")).thenReturn(recurso);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            despachoService.despachar(despachoDTO);
        });

        assertEquals("Stock insuficiente para AGUA", exception.getMessage());
        // Verificamos que el proceso se detuvo y no se descontó nada
        verify(recursoService, never()).descontarStock(anyString(), anyInt());
    }

    @Test
    void listar_DebeRetornarListaDeEnvios() {
        // Arrange
        List<Envio> listaEsperada = Arrays.asList(new Envio(), new Envio());
        when(envioService.listar()).thenReturn(listaEsperada);

        // Act
        List<Envio> resultado = despachoService.listar();

        // Assert
        assertEquals(2, resultado.size());
        verify(envioService, times(1)).listar();
    }
}