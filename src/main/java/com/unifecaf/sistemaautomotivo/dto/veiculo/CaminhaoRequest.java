package com.unifecaf.sistemaautomotivo.dto.veiculo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class CaminhaoRequest extends VeiculoRequest {

    @NotNull(message = "A capacidade de carga é obrigatória")
    @Positive(message = "A capacidade de carga deve ser positiva")
    private BigDecimal capacidadeCargaKg;

    @NotNull(message = "O número de eixos é obrigatório")
    @Positive(message = "O número de eixos deve ser positivo")
    private Integer numeroEixos;

    public BigDecimal getCapacidadeCargaKg() {
        return capacidadeCargaKg;
    }

    public void setCapacidadeCargaKg(BigDecimal capacidadeCargaKg) {
        this.capacidadeCargaKg = capacidadeCargaKg;
    }

    public Integer getNumeroEixos() {
        return numeroEixos;
    }

    public void setNumeroEixos(Integer numeroEixos) {
        this.numeroEixos = numeroEixos;
    }
}
