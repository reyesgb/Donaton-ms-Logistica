package com.donaton.logistica.dto;

import lombok.Data;

@Data
public class DonacionDTO {

    private Long id;
    private String categoria;
    private Integer cantidad;
    private String estado;
}


