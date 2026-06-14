package com.unifecaf.sistemaautomotivo.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.OffsetDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ApiError(
        OffsetDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        List<CampoInvalido> errors) {

    public record CampoInvalido(String campo, String mensagem) {
    }

    public static ApiError de(int status, String error, String message, String path) {
        return new ApiError(OffsetDateTime.now(), status, error, message, path, List.of());
    }

    public static ApiError deValidacao(int status, String error, String message, String path,
            List<CampoInvalido> errors) {
        return new ApiError(OffsetDateTime.now(), status, error, message, path, errors);
    }
}
