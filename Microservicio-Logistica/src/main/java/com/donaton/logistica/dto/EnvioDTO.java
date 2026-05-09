package com.donaton.logistica.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EnvioDTO {

    @NotBlank
    private String destino;

    @NotBlank
    private String estado;

    @NotBlank
    private String transporte;

    @Min(1)
    private int cantidad;
}