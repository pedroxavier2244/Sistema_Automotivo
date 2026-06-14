package com.unifecaf.sistemaautomotivo.dto.marca;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MarcaRequest(
        @NotBlank(message = "O nome da marca é obrigatório")
        @Size(max = 80, message = "O nome da marca deve ter no máximo 80 caracteres")
        String nome) {
}
