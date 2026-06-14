package com.unifecaf.sistemaautomotivo.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.unifecaf.sistemaautomotivo.domain.Marca;
import com.unifecaf.sistemaautomotivo.domain.Modelo;
import com.unifecaf.sistemaautomotivo.dto.marca.MarcaRequest;
import com.unifecaf.sistemaautomotivo.dto.marca.MarcaResponse;
import com.unifecaf.sistemaautomotivo.dto.modelo.ModeloRequest;
import com.unifecaf.sistemaautomotivo.dto.modelo.ModeloResponse;
import org.junit.jupiter.api.Test;

class MapperTest {

    private final MarcaMapper marcaMapper = new MarcaMapper();
    private final ModeloMapper modeloMapper = new ModeloMapper();

    @Test
    void marcaRequestViraEntidade() {
        Marca marca = marcaMapper.toEntity(new MarcaRequest("Toyota"));
        assertThat(marca.getNome()).isEqualTo("Toyota");
    }

    @Test
    void marcaEntidadeViraResponse() {
        MarcaResponse response = marcaMapper.toResponse(new Marca("Honda"));
        assertThat(response.nome()).isEqualTo("Honda");
    }

    @Test
    void modeloRequestViraEntidadeComMarca() {
        Marca marca = new Marca("Toyota");
        Modelo modelo = modeloMapper.toEntity(new ModeloRequest("Corolla", 1L), marca);
        assertThat(modelo.getNome()).isEqualTo("Corolla");
        assertThat(modelo.getMarca()).isSameAs(marca);
    }

    @Test
    void modeloEntidadeViraResponseComDadosDaMarca() {
        Modelo modelo = new Modelo("Civic", new Marca("Honda"));
        ModeloResponse response = modeloMapper.toResponse(modelo);
        assertThat(response.nome()).isEqualTo("Civic");
        assertThat(response.marcaNome()).isEqualTo("Honda");
    }
}
