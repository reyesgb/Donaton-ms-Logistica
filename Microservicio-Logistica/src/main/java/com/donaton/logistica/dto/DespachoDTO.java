package com.donaton.logistica.dto;

import lombok.Data;

@Data
public class DespachoDTO {

    private Long necesidadId;

    private String categoria;

    private Integer cantidad;

    private String destino;

    private String transporte;

    private String responsable;

    private String observaciones;
}
