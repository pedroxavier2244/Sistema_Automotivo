package com.unifecaf.sistemaautomotivo.dto.veiculo;

import com.unifecaf.sistemaautomotivo.domain.StatusVeiculo;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public record AtualizarVeiculoRequest(
        @Positive(message = "O preço deve ser positivo")
        BigDecimal preco,

        @PositiveOrZero(message = "A quilometragem não pode ser negativa")
        Integer quilometragem,

        StatusVeiculo status) {
}
