package com.unifecaf.sistemaautomotivo.exception;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.validation.Valid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.GetMapping;

class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new TestController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void recursoInexistenteRetorna404() throws Exception {
        mockMvc.perform(get("/test/nao-encontrado"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.path").value("/test/nao-encontrado"));
    }

    @Test
    void violacaoDeRegraRetorna409() throws Exception {
        mockMvc.perform(get("/test/regra"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void corpoInvalidoRetorna400ComCampos() throws Exception {
        mockMvc.perform(post("/test/validar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors[0].campo").value("nome"))
                .andExpect(jsonPath("$.errors[0].mensagem").exists());
    }

    @RestController
    static class TestController {

        @GetMapping("/test/nao-encontrado")
        void naoEncontrado() {
            throw ResourceNotFoundException.de("Veículo", 99);
        }

        @GetMapping("/test/regra")
        void regra() {
            throw new BusinessException("Regra violada");
        }

        @PostMapping("/test/validar")
        void validar(@Valid @RequestBody Payload payload) {

        }
    }

    record Payload(@NotBlank String nome) {
    }
}
