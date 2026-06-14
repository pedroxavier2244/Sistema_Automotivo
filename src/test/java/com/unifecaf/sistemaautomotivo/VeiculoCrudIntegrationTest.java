package com.unifecaf.sistemaautomotivo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class VeiculoCrudIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private long extrairId(String json) throws Exception {
        JsonNode node = objectMapper.readTree(json);
        return node.get("id").asLong();
    }

    @Test
    void fluxoCompletoCriarConsultarAtualizarRemover() throws Exception {

        String marcaJson = mockMvc.perform(post("/api/marcas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Toyota\"}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        long marcaId = extrairId(marcaJson);

        String modeloJson = mockMvc.perform(post("/api/modelos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Corolla\",\"marcaId\":" + marcaId + "}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        long modeloId = extrairId(modeloJson);

        String carroBody = """
                {
                  "tipo": "CARRO",
                  "modeloId": %d,
                  "anoFabricacao": 2022,
                  "cor": "Preto",
                  "preco": 120000,
                  "quilometragem": 15000,
                  "status": "DISPONIVEL",
                  "numeroPortas": 4,
                  "tipoCombustivel": "Flex"
                }
                """.formatted(modeloId);
        String veiculoJson = mockMvc.perform(post("/api/veiculos")
                        .contentType(MediaType.APPLICATION_JSON).content(carroBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipo").value("CARRO"))
                .andExpect(jsonPath("$.marcaNome").value("Toyota"))
                .andReturn().getResponse().getContentAsString();
        long veiculoId = extrairId(veiculoJson);

        mockMvc.perform(get("/api/veiculos")
                        .param("status", "DISPONIVEL").param("tipo", "CARRO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].id").value(veiculoId));

        mockMvc.perform(patch("/api/veiculos/" + veiculoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"preco\": 99000, \"status\": \"RESERVADO\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.preco").value(99000))
                .andExpect(jsonPath("$.status").value("RESERVADO"));

        mockMvc.perform(delete("/api/veiculos/" + veiculoId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/veiculos/" + veiculoId))
                .andExpect(status().isNotFound());
    }
}
