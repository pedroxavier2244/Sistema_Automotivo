package com.unifecaf.sistemaautomotivo.dto.veiculo;

import com.unifecaf.sistemaautomotivo.domain.StatusVeiculo;
import java.math.BigDecimal;

public record VeiculoFiltro(
        String marca,
        String modelo,
        BigDecimal precoMin,
        BigDecimal precoMax,
        Integer ano,
        StatusVeiculo status,
        String tipo) {
}
