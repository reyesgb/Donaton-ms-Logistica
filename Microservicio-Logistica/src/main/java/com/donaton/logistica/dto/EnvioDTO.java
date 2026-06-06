package com.donaton.logistica.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EnvioDTO {

    @NotNull
    private Long necesidadId;

    @NotBlank
    private String categoria;

    @Min(1)
    private Integer cantidadDespachada;

    @NotBlank
    private String destino;
}