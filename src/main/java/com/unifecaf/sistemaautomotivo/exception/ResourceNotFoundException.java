package com.unifecaf.sistemaautomotivo.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String mensagem) {
        super(mensagem);
    }

    public static ResourceNotFoundException de(String recurso, Object id) {
        return new ResourceNotFoundException(recurso + " não encontrado(a) para o id " + id);
    }
}
