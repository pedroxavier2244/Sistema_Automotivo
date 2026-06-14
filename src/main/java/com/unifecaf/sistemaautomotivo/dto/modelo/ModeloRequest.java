package com.unifecaf.sistemaautomotivo.dto.modelo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ModeloRequest(
        @NotBlank(message = "O nome do modelo é obrigatório")
        @Size(max = 80, message = "O nome do modelo deve ter no máximo 80 caracteres")
        String nome,

        @NotNull(message = "A marca (marcaId) é obrigatória")
        Long marcaId) {
}
