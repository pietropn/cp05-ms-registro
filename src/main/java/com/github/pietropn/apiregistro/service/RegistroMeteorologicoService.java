package com.github.pietropn.apiregistro.service;

import com.github.pietropn.apiregistro.dto.RegistroMeteorologicoRequestDTO;
import com.github.pietropn.apiregistro.dto.RegistroMeteorologicoResponseDTO;
import com.github.pietropn.apiregistro.entity.RegistroMeteorologico;
import com.github.pietropn.apiregistro.exception.RecursoNaoEncontradoException;
import com.github.pietropn.apiregistro.repository.RegistroMeteorologicoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RegistroMeteorologicoService {

    private final RegistroMeteorologicoRepository repository;

    public RegistroMeteorologicoService(RegistroMeteorologicoRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<RegistroMeteorologicoResponseDTO> listarTodos() {
        return repository.findAll()
                .stream()
                .map(RegistroMeteorologicoResponseDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public RegistroMeteorologicoResponseDTO buscarPorId(Long id) {
        return RegistroMeteorologicoResponseDTO.fromEntity(buscarEntidade(id));
    }

    @Transactional
    public RegistroMeteorologicoResponseDTO criar(RegistroMeteorologicoRequestDTO dto) {
        RegistroMeteorologico entity = new RegistroMeteorologico();
        copiarDados(dto, entity);
        return RegistroMeteorologicoResponseDTO.fromEntity(repository.save(entity));
    }

    @Transactional
    public RegistroMeteorologicoResponseDTO atualizar(Long id, RegistroMeteorologicoRequestDTO dto) {
        RegistroMeteorologico entity = buscarEntidade(id);
        copiarDados(dto, entity);
        return RegistroMeteorologicoResponseDTO.fromEntity(repository.save(entity));
    }

    @Transactional
    public void excluir(Long id) {
        RegistroMeteorologico entity = buscarEntidade(id);
        repository.delete(entity);
    }

    private RegistroMeteorologico buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Registro meteorológico com id " + id + " não encontrado"));
    }

    private void copiarDados(RegistroMeteorologicoRequestDTO dto, RegistroMeteorologico entity) {
        entity.setEstacaoNome(dto.estacaoNome().trim());
        entity.setDataRegistro(dto.dataRegistro());
        entity.setTemperatura(dto.temperatura());
        entity.setPercentualUmidade(dto.percentualUmidade());
        entity.setPrecipitacao(dto.precipitacao());
        entity.setCondicaoClimatica(dto.condicaoClimatica());
    }
}
