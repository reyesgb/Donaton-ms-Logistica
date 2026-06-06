package com.donaton.logistica.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "envios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long necesidadId;

    private String categoria;

    private Integer cantidadDespachada;

    private String destino;

    private LocalDate fecha;

}