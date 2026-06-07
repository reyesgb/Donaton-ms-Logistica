package com.donaton.logistica.service;

import com.donaton.logistica.model.Recurso;
import com.donaton.logistica.repository.RecursoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecursoServiceTest {

    @Mock
    private RecursoRepository repository;

    @InjectMocks
    private RecursoService recursoService;

    private Recurso recursoExistente;

    @BeforeEach
    void setUp() {
        recursoExistente = new Recurso();
        recursoExistente.setCategoria("ROPA");
        recursoExistente.setCantidadDisponible(20);
    }

    // --- Pruebas para agregarStock ---

    @Test
    void agregarStock_DebeCrearRecurso_CuandoCategoriaNoExiste() {
        // Arrange: El repositorio no encuentra la categoría (Optional vacío)
        when(repository.findByCategoriaIgnoreCase("ROPA")).thenReturn(Optional.empty());
        // Simulamos el guardado
        when(repository.save(any(Recurso.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Recurso resultado = recursoService.agregarStock("ROPA", 15);

        // Assert
        assertNotNull(resultado);
        assertEquals("ROPA", resultado.getCategoria());
        assertEquals(15, resultado.getCantidadDisponible()); // Toma la cantidad inicial porque era null
        verify(repository, times(1)).save(any(Recurso.class));
    }

    @Test
    void agregarStock_DebeSumarCantidad_CuandoCategoriaYaExiste() {
        // Arrange: El repositorio encuentra la categoría con 20 unidades
        when(repository.findByCategoriaIgnoreCase("ROPA")).thenReturn(Optional.of(recursoExistente));
        when(repository.save(any(Recurso.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Recurso resultado = recursoService.agregarStock("ROPA", 10);

        // Assert
        assertEquals("ROPA", resultado.getCategoria());
        assertEquals(30, resultado.getCantidadDisponible()); // 20 iniciales + 10 nuevos
        verify(repository, times(1)).save(recursoExistente);
    }

    // --- Pruebas para descontarStock ---

    @Test
    void descontarStock_DebeRestarCantidad_CuandoHayStockSuficiente() {
        // Arrange: Hay 20 unidades, queremos descontar 5
        when(repository.findByCategoriaIgnoreCase("ROPA")).thenReturn(Optional.of(recursoExistente));
        when(repository.save(any(Recurso.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Recurso resultado = recursoService.descontarStock("ROPA", 5);

        // Assert
        assertEquals("ROPA", resultado.getCategoria());
        assertEquals(15, resultado.getCantidadDisponible()); // 20 iniciales - 5 descontados
        verify(repository, times(1)).save(recursoExistente);
    }

    @Test
    void descontarStock_DebeLanzarExcepcion_CuandoCategoriaNoExiste() {
        // Arrange: No se encuentra la categoría
        when(repository.findByCategoriaIgnoreCase("ROPA")).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            recursoService.descontarStock("ROPA", 5);
        });

        assertEquals("No existe stock para la categoria", exception.getMessage());
        verify(repository, never()).save(any()); // Nos aseguramos de que no guarde nada
    }

    @Test
    void descontarStock_DebeLanzarExcepcion_CuandoStockEsInsuficiente() {
        // Arrange: Hay 20 unidades, pero pedimos 50
        when(repository.findByCategoriaIgnoreCase("ROPA")).thenReturn(Optional.of(recursoExistente));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            recursoService.descontarStock("ROPA", 50);
        });

        assertEquals("Stock insuficiente", exception.getMessage());
        verify(repository, never()).save(any());
    }

    // --- Pruebas para buscarPorCategoria ---

    @Test
    void buscarPorCategoria_DebeRetornarRecurso_CuandoExiste() {
        // Arrange
        when(repository.findByCategoriaIgnoreCase("ROPA")).thenReturn(Optional.of(recursoExistente));

        // Act
        Recurso resultado = recursoService.buscarPorCategoria("ROPA");

        // Assert
        assertNotNull(resultado);
        assertEquals("ROPA", resultado.getCategoria());
    }

    @Test
    void buscarPorCategoria_DebeRetornarNull_CuandoNoExiste() {
        // Arrange
        when(repository.findByCategoriaIgnoreCase("ROPA")).thenReturn(Optional.empty());

        // Act
        Recurso resultado = recursoService.buscarPorCategoria("ROPA");

        // Assert
        assertNull(resultado);
    }

    // --- Prueba para listar ---

    @Test
    void listar_DebeRetornarListaDeRecursos() {
        // Arrange
        List<Recurso> listaEsperada = Arrays.asList(recursoExistente, new Recurso());
        when(repository.findAll()).thenReturn(listaEsperada);

        // Act
        List<Recurso> resultado = recursoService.listar();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(repository, times(1)).findAll();
    }
}