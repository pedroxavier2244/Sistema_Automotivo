package com.unifecaf.sistemaautomotivo.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class MarcaModeloMappingTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void devePersistirModeloAssociadoAMarca() {
        Marca toyota = new Marca("Toyota");
        Modelo corolla = new Modelo("Corolla", toyota);

        entityManager.persist(toyota);
        Long modeloId = entityManager.persistAndGetId(corolla, Long.class);
        entityManager.flush();
        entityManager.clear();

        Modelo recarregado = entityManager.find(Modelo.class, modeloId);
        assertThat(recarregado.getNome()).isEqualTo("Corolla");
        assertThat(recarregado.getMarca().getNome()).isEqualTo("Toyota");
    }

    @Test
    void marcaDeveTerNomeUnico() {
        entityManager.persistAndFlush(new Marca("Honda"));

        assertThatThrownBy(() -> entityManager.persistAndFlush(new Marca("Honda")))
                .isInstanceOf(PersistenceException.class);
    }

    @Test
    void modeloDeveExigirMarca() {
        assertThatThrownBy(() -> entityManager.persistAndFlush(new Modelo("Sem Marca", null)))
                .isInstanceOf(Exception.class);
    }
}
