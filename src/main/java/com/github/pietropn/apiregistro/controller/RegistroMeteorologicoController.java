package com.github.pietropn.apiregistro.controller;

import com.github.pietropn.apiregistro.dto.RegistroMeteorologicoRequestDTO;
import com.github.pietropn.apiregistro.dto.RegistroMeteorologicoResponseDTO;
import com.github.pietropn.apiregistro.service.RegistroMeteorologicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/registros")
@Tag(name = "Registros Meteorológicos", description = "Operações de CRUD de medições meteorológicas")
public class RegistroMeteorologicoController {

    private final RegistroMeteorologicoService service;

    public RegistroMeteorologicoController(RegistroMeteorologicoService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Lista todos os registros meteorológicos")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public ResponseEntity<List<RegistroMeteorologicoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um registro meteorológico pelo id")
    @ApiResponse(responseCode = "200", description = "Registro encontrado")
    @ApiResponse(responseCode = "404", description = "Registro não encontrado")
    public ResponseEntity<RegistroMeteorologicoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Cadastra um novo registro meteorológico")
    @ApiResponse(responseCode = "201", description = "Registro criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    public ResponseEntity<RegistroMeteorologicoResponseDTO> criar(
            @Valid @RequestBody RegistroMeteorologicoRequestDTO dto) {
        RegistroMeteorologicoResponseDTO criado = service.criar(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(criado.registroId())
                .toUri();
        return ResponseEntity.created(location).body(criado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um registro meteorológico existente")
    @ApiResponse(responseCode = "200", description = "Registro atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "404", description = "Registro não encontrado")
    public ResponseEntity<RegistroMeteorologicoResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody RegistroMeteorologicoRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui um registro meteorológico")
    @ApiResponse(responseCode = "204", description = "Registro excluído com sucesso")
    @ApiResponse(responseCode = "404", description = "Registro não encontrado")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
