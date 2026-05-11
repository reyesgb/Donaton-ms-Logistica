package com.donaton.logistica.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "envios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String destino;
    private String descripcionAyuda;
    private String estado;
    private String transporte;
    private int cantidad;
}