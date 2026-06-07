package com.donaton.logistica.controller;

import com.donaton.logistica.service.RecepcionDonacionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LogisticaController.class)
class LogisticaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecepcionDonacionService service;

    @Test
    @WithMockUser
    void recepcionar_DebeRetornarMensajeExitoso() throws Exception {
        // Arrange
        Long idDonacion = 5L;

        // No necesitamos 'when(...)' porque el método es void,
        // pero verificaremos que se llame correctamente.

        // Act & Assert
        mockMvc.perform(post("/logistica/recepcionar/{idDonacion}", idDonacion)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Donación agregada al stock"));

        verify(service, times(1)).recepcionar(idDonacion);
    }
}