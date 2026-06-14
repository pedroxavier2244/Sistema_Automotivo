package com.unifecaf.sistemaautomotivo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unifecaf.sistemaautomotivo.dto.marca.MarcaRequest;
import com.unifecaf.sistemaautomotivo.dto.marca.MarcaResponse;
import com.unifecaf.sistemaautomotivo.exception.ResourceNotFoundException;
import com.unifecaf.sistemaautomotivo.service.MarcaService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MarcaController.class)
class MarcaControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private MarcaService service;

    @Test
    void postCriaMarcaERetorna201ComLocation() throws Exception {
        when(service.criar(any())).thenReturn(new MarcaResponse(1L, "Toyota"));

        mockMvc.perform(post("/api/marcas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new MarcaRequest("Toyota"))))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/marcas/1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Toyota"));
    }

    @Test
    void postComNomeEmBrancoRetorna400() throws Exception {
        mockMvc.perform(post("/api/marcas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getListaMarcas() throws Exception {
        when(service.listar()).thenReturn(List.of(new MarcaResponse(1L, "Toyota")));

        mockMvc.perform(get("/api/marcas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Toyota"));
    }

    @Test
    void getPorIdInexistenteRetorna404() throws Exception {
        when(service.buscarPorId(99L)).thenThrow(ResourceNotFoundException.de("Marca", 99));

        mockMvc.perform(get("/api/marcas/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void deleteRetorna204() throws Exception {
        mockMvc.perform(delete("/api/marcas/1"))
                .andExpect(status().isNoContent());
        verify(service).remover(eq(1L));
    }
}
