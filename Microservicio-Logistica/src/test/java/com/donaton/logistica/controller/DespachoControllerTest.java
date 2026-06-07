package com.donaton.logistica.controller;

import com.donaton.logistica.dto.DespachoDTO;
import com.donaton.logistica.model.Envio;
import com.donaton.logistica.service.DespachoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DespachoController.class)
class DespachoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DespachoService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void despachar_DebeRetornar200YEnvioCreado() throws Exception {
        // Arrange
        DespachoDTO dto = new DespachoDTO();
        // Ajusta estos campos según los que tenga tu DespachoDTO

        Envio envioCreado = new Envio();
        envioCreado.setId(100L);
        envioCreado.setDestino("Santiago");
        envioCreado.setFecha(LocalDate.now());

        when(service.despachar(any(DespachoDTO.class))).thenReturn(envioCreado);

        // Act & Assert
        mockMvc.perform(post("/despachos")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100L))
                .andExpect(jsonPath("$.destino").value("Santiago"));

        verify(service, times(1)).despachar(any(DespachoDTO.class));
    }

    @Test
    @WithMockUser
    void listar_DebeRetornarListaDeEnvios() throws Exception {
        // Arrange
        Envio envio1 = new Envio();
        envio1.setId(1L);
        envio1.setDestino("Valparaíso");

        Envio envio2 = new Envio();
        envio2.setId(2L);
        envio2.setDestino("Concepción");

        List<Envio> envios = Arrays.asList(envio1, envio2);

        when(service.listar()).thenReturn(envios);

        // Act & Assert
        mockMvc.perform(get("/despachos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].destino").value("Valparaíso"))
                .andExpect(jsonPath("$[1].destino").value("Concepción"));

        verify(service, times(1)).listar();
    }
}