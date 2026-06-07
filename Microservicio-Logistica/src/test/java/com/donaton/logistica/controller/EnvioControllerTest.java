package com.donaton.logistica.controller;

import com.donaton.logistica.dto.EnvioDTO;
import com.donaton.logistica.model.Envio;
import com.donaton.logistica.service.EnvioService;
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

@WebMvcTest(EnvioController.class)
class EnvioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EnvioService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void crear_CuandoDtoEsValido_DebeRetornar200YEnvioGuardado() throws Exception {
        // Arrange
        EnvioDTO dto = new EnvioDTO();
        dto.setNecesidadId(1L);
        dto.setCategoria("Alimentos");
        dto.setCantidadDespachada(10);
        dto.setDestino("Santiago");

        Envio envioGuardado = new Envio();
        envioGuardado.setId(1L);
        envioGuardado.setNecesidadId(1L);
        envioGuardado.setDestino("Santiago");
        envioGuardado.setFecha(LocalDate.now());

        when(service.guardar(any(Envio.class))).thenReturn(envioGuardado);

        // Act & Assert
        mockMvc.perform(post("/logistica")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.destino").value("Santiago"));

        verify(service, times(1)).guardar(any(Envio.class));
    }

    @Test
    @WithMockUser
    void listar_DebeRetornarListaDeEnvios() throws Exception {
        // Arrange
        Envio e1 = new Envio(); e1.setId(1L); e1.setDestino("Santiago");
        Envio e2 = new Envio(); e2.setId(2L); e2.setDestino("Valparaíso");
        List<Envio> lista = Arrays.asList(e1, e2);

        when(service.listar()).thenReturn(lista);

        // Act & Assert
        mockMvc.perform(get("/logistica"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].destino").value("Valparaíso"));

        verify(service, times(1)).listar();
    }
}