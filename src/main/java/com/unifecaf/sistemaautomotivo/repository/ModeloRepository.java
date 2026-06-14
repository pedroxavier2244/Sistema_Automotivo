package com.unifecaf.sistemaautomotivo.repository;

import com.unifecaf.sistemaautomotivo.domain.Modelo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModeloRepository extends JpaRepository<Modelo, Long> {

    List<Modelo> findByMarcaId(Long marcaId);

    boolean existsByMarcaId(Long marcaId);
}
