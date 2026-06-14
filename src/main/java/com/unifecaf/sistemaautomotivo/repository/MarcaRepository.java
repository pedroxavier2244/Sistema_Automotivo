package com.unifecaf.sistemaautomotivo.repository;

import com.unifecaf.sistemaautomotivo.domain.Marca;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarcaRepository extends JpaRepository<Marca, Long> {

    boolean existsByNomeIgnoreCase(String nome);

    Optional<Marca> findByNomeIgnoreCase(String nome);
}
