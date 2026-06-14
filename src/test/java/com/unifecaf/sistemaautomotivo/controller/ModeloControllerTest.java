package com.unifecaf.sistemaautomotivo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.unifecaf.sistemaautomotivo.dto.modelo.ModeloResponse;
import com.unifecaf.sistemaautomotivo.service.ModeloService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ModeloController.class)
class ModeloControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ModeloService service;

    @Test
    void postCriaModeloRetorna201ComLocation() throws Exception {
        when(service.criar(any())).thenReturn(new ModeloResponse(1L, "Corolla", 1L, "Toyota"));

        mockMvc.perform(post("/api/modelos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Corolla\",\"marcaId\":1}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/modelos/1"))
                .andExpect(jsonPath("$.marcaNome").value("Toyota"));
    }

    @Test
    void postSemMarcaIdRetorna400() throws Exception {
        mockMvc.perform(post("/api/modelos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Corolla\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getListaModelosFiltrandoPorMarca() throws Exception {
        when(service.listar(eq(1L)))
                .thenReturn(List.of(new ModeloResponse(1L, "Corolla", 1L, "Toyota")));

        mockMvc.perform(get("/api/modelos").param("marcaId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Corolla"));
        verify(service).listar(eq(1L));
    }
}
