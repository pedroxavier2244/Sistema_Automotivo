package com.unifecaf.sistemaautomotivo.repository;

import com.unifecaf.sistemaautomotivo.domain.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface VeiculoRepository
        extends JpaRepository<Veiculo, Long>, JpaSpecificationExecutor<Veiculo> {

    boolean existsByModeloId(Long modeloId);
}
