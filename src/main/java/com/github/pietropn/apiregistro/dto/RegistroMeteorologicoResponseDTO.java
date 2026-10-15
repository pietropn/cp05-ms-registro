package com.github.pietropn.apiregistro.dto;

import com.github.pietropn.apiregistro.entity.CondicaoClimatica;
import com.github.pietropn.apiregistro.entity.RegistroMeteorologico;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Registro meteorológico retornado pela API")
public record RegistroMeteorologicoResponseDTO(
        Long registroId,
        String estacaoNome,
        LocalDate dataRegistro,
        BigDecimal temperatura,
        BigDecimal percentualUmidade,
        BigDecimal precipitacao,
        CondicaoClimatica condicaoClimatica
) {

    public static RegistroMeteorologicoResponseDTO fromEntity(RegistroMeteorologico entity) {
        return new RegistroMeteorologicoResponseDTO(
                entity.getRegistroId(),
                entity.getEstacaoNome(),
                entity.getDataRegistro(),
                entity.getTemperatura(),
                entity.getPercentualUmidade(),
                entity.getPrecipitacao(),
                entity.getCondicaoClimatica()
        );
    }
}
