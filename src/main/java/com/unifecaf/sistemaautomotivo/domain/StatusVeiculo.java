package com.unifecaf.sistemaautomotivo.domain;

public enum StatusVeiculo {

    DISPONIVEL(false),

    RESERVADO(false),

    VENDIDO(true),

    MANUTENCAO(false);

    private final boolean finalState;

    StatusVeiculo(boolean finalState) {
        this.finalState = finalState;
    }

    public boolean isFinal() {
        return finalState;
    }
}
