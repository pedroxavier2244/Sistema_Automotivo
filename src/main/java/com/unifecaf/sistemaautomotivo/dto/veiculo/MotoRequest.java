package com.unifecaf.sistemaautomotivo.dto.veiculo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class MotoRequest extends VeiculoRequest {

    @NotNull(message = "A cilindrada é obrigatória")
    @Positive(message = "A cilindrada deve ser positiva")
    private Integer cilindrada;

    public Integer getCilindrada() {
        return cilindrada;
    }

    public void setCilindrada(Integer cilindrada) {
        this.cilindrada = cilindrada;
    }
}
