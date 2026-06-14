package com.unifecaf.sistemaautomotivo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.unifecaf.sistemaautomotivo.domain.StatusVeiculo;
import com.unifecaf.sistemaautomotivo.dto.veiculo.VeiculoResponse;
import com.unifecaf.sistemaautomotivo.service.VeiculoService;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(VeiculoController.class)
@Import(SpringDataWebAutoConfiguration.class)
class VeiculoControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private VeiculoService service;

    private VeiculoResponse carroResponse() {
        return new VeiculoResponse(1L, "CARRO", 1L, "Corolla", 1L, "Toyota",
                2022, "Preto", new BigDecimal("120000"), 15000, StatusVeiculo.DISPONIVEL,
                4, "Flex", null, null, null);
    }

    @Test
    void postCarroRetorna201() throws Exception {
        when(service.criar(any())).thenReturn(carroResponse());

        String body = """
                {
                  "tipo": "CARRO",
                  "modeloId": 1,
                  "anoFabricacao": 2022,
                  "cor": "Preto",
                  "preco": 120000,
                  "quilometragem": 15000,
                  "status": "DISPONIVEL",
                  "numeroPortas": 4,
                  "tipoCombustivel": "Flex"
                }
                """;

        mockMvc.perform(post("/api/veiculos")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipo").value("CARRO"))
                .andExpect(jsonPath("$.numeroPortas").value(4));
    }

    @Test
    void postSemTipoRetorna400() throws Exception {
        String body = """
                { "modeloId": 1, "anoFabricacao": 2022, "cor": "Preto",
                  "preco": 120000, "quilometragem": 15000, "status": "DISPONIVEL" }
                """;
        mockMvc.perform(post("/api/veiculos")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getConsultaComFiltrosRetornaPagina() throws Exception {
        Page<VeiculoResponse> page = new PageImpl<>(List.of(carroResponse()));
        when(service.consultar(any(), any())).thenReturn(page);

        mockMvc.perform(get("/api/veiculos")
                        .param("status", "DISPONIVEL")
                        .param("tipo", "CARRO")
                        .param("precoMax", "200000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].tipo").value("CARRO"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void patchAtualizaVeiculo() throws Exception {
        when(service.atualizar(eq(1L), any())).thenReturn(carroResponse());

        mockMvc.perform(patch("/api/veiculos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"preco\": 99000, \"status\": \"RESERVADO\"}"))
                .andExpect(status().isOk());
        verify(service).atualizar(eq(1L), any());
    }

    @Test
    void deleteRetorna204() throws Exception {
        mockMvc.perform(delete("/api/veiculos/1"))
                .andExpect(status().isNoContent());
        verify(service).remover(eq(1L));
    }
}
