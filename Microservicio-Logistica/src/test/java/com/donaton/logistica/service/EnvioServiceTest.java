package com.donaton.logistica.service;

import com.donaton.logistica.model.Envio;
import com.donaton.logistica.repository.EnvioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnvioServiceTest {

    @Mock
    private EnvioRepository repository;

    @InjectMocks
    private EnvioService envioService;

    private Envio envioMock;

    @BeforeEach
    void setUp() {
        envioMock = new Envio();
        envioMock.setCategoria("ALIMENTOS");
        envioMock.setCantidadDespachada(50);
        // Si tienes un ID, puedes setearlo aquí para simular que ya se guardó en BD
    }

    @Test
    void guardar_DebeGuardarYRetornarEnvio() {
        // Arrange
        when(repository.save(any(Envio.class))).thenReturn(envioMock);

        // Act
        Envio resultado = envioService.guardar(new Envio());

        // Assert
        assertNotNull(resultado);
        assertEquals("ALIMENTOS", resultado.getCategoria());
        assertEquals(50, resultado.getCantidadDespachada());

        // Verificamos que el repositorio fue llamado exactamente una vez con el método save
        verify(repository, times(1)).save(any(Envio.class));
    }

    @Test
    void listar_DebeRetornarListaDeEnvios() {
        // Arrange
        List<Envio> listaEsperada = Arrays.asList(envioMock, new Envio());
        when(repository.findAll()).thenReturn(listaEsperada);

        // Act
        List<Envio> resultado = envioService.listar();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());

        // Verificamos que el repositorio fue llamado exactamente una vez con el método findAll
        verify(repository, times(1)).findAll();
    }
}