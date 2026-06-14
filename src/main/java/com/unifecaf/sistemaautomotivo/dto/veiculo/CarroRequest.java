package com.unifecaf.sistemaautomotivo.dto.veiculo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class CarroRequest extends VeiculoRequest {

    @NotNull(message = "O número de portas é obrigatório")
    @Positive(message = "O número de portas deve ser positivo")
    private Integer numeroPortas;

    @NotBlank(message = "O tipo de combustível é obrigatório")
    @Size(max = 30)
    private String tipoCombustivel;

    public Integer getNumeroPortas() {
        return numeroPortas;
    }

    public void setNumeroPortas(Integer numeroPortas) {
        this.numeroPortas = numeroPortas;
    }

    public String getTipoCombustivel() {
        return tipoCombustivel;
    }

    public void setTipoCombustivel(String tipoCombustivel) {
        this.tipoCombustivel = tipoCombustivel;
    }
}
