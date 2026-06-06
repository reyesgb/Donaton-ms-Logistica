package com.donaton.logistica.dto;

import lombok.Data;

@Data
public class NecesidadDTO {

    private Long id;

    private String comuna;

    private String categoria;

    private Integer cantidadNecesaria;

    private String descripcion;

    private String prioridad;

    private String estado;
}