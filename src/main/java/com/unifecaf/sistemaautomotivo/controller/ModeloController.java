package com.unifecaf.sistemaautomotivo.controller;

import com.unifecaf.sistemaautomotivo.dto.modelo.ModeloRequest;
import com.unifecaf.sistemaautomotivo.dto.modelo.ModeloResponse;
import com.unifecaf.sistemaautomotivo.service.ModeloService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/modelos")
@Tag(name = "Modelos", description = "Cadastro de modelos de veículos")
public class ModeloController {

    private final ModeloService service;

    public ModeloController(ModeloService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Cadastra um novo modelo")
    public ResponseEntity<ModeloResponse> criar(@Valid @RequestBody ModeloRequest request) {
        ModeloResponse criado = service.criar(request);
        return ResponseEntity.created(URI.create("/api/modelos/" + criado.id())).body(criado);
    }

    @GetMapping
    @Operation(summary = "Lista modelos, opcionalmente filtrando por marca")
    public List<ModeloResponse> listar(@RequestParam(required = false) Long marcaId) {
        return service.listar(marcaId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um modelo por id")
    public ModeloResponse buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um modelo")
    public ModeloResponse atualizar(@PathVariable Long id,
            @Valid @RequestBody ModeloRequest request) {
        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove um modelo")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
