package com.unifecaf.sistemaautomotivo.repository;

import com.unifecaf.sistemaautomotivo.domain.Marca;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarcaRepository extends JpaRepository<Marca, Long> {

    boolean existsByNomeIgnoreCase(String nome);
}
