package com.unifecaf.sistemaautomotivo.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class StatusVeiculoTest {

    @Test
    void deveConterOsStatusEsperados() {
        assertThat(StatusVeiculo.values())
                .containsExactlyInAnyOrder(
                        StatusVeiculo.DISPONIVEL,
                        StatusVeiculo.RESERVADO,
                        StatusVeiculo.VENDIDO,
                        StatusVeiculo.MANUTENCAO);
    }

    @Test
    void deveResolverStatusPeloNome() {
        assertThat(StatusVeiculo.valueOf("DISPONIVEL")).isEqualTo(StatusVeiculo.DISPONIVEL);
    }

    @Test
    void vendidoDeveSerStatusFinal() {
        assertThat(StatusVeiculo.VENDIDO.isFinal()).isTrue();
    }

    @Test
    void disponivelNaoDeveSerStatusFinal() {
        assertThat(StatusVeiculo.DISPONIVEL.isFinal()).isFalse();
    }

    @Test
    void nomeInvalidoDeveLancarExcecao() {
        assertThatThrownBy(() -> StatusVeiculo.valueOf("INEXISTENTE"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
