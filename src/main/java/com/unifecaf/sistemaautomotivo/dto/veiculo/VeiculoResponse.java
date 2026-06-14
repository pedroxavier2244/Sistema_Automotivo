package com.unifecaf.sistemaautomotivo.dto.veiculo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.unifecaf.sistemaautomotivo.domain.StatusVeiculo;
import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record VeiculoResponse(
        Long id,
        String tipo,
        Long modeloId,
        String modeloNome,
        Long marcaId,
        String marcaNome,
        int anoFabricacao,
        String cor,
        BigDecimal preco,
        int quilometragem,
        StatusVeiculo status,

        Integer numeroPortas,
        String tipoCombustivel,

        Integer cilindrada,

        BigDecimal capacidadeCargaKg,
        Integer numeroEixos) {
}
