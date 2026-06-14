package com.unifecaf.sistemaautomotivo.controller;

import com.unifecaf.sistemaautomotivo.dto.marca.MarcaRequest;
import com.unifecaf.sistemaautomotivo.dto.marca.MarcaResponse;
import com.unifecaf.sistemaautomotivo.service.MarcaService;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/marcas")
@Tag(name = "Marcas", description = "Cadastro de marcas de veículos")
public class MarcaController {

    private final MarcaService service;

    public MarcaController(MarcaService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Cadastra uma nova marca")
    public ResponseEntity<MarcaResponse> criar(@Valid @RequestBody MarcaRequest request) {
        MarcaResponse criada = service.criar(request);
        return ResponseEntity.created(URI.create("/api/marcas/" + criada.id())).body(criada);
    }

    @GetMapping
    @Operation(summary = "Lista todas as marcas")
    public List<MarcaResponse> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma marca por id")
    public MarcaResponse buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza uma marca")
    public MarcaResponse atualizar(@PathVariable Long id, @Valid @RequestBody MarcaRequest request) {
        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove uma marca")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
