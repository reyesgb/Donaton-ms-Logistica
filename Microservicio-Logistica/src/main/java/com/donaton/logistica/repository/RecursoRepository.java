package com.donaton.logistica.repository;

import com.donaton.logistica.model.Recurso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecursoRepository extends JpaRepository<Recurso, Long> {

    Optional<Recurso> findByCategoriaIgnoreCase(String categoria);
}