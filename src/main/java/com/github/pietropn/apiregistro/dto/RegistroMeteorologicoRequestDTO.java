package com.github.pietropn.apiregistro.dto;

import com.github.pietropn.apiregistro.entity.CondicaoClimatica;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Dados para cadastro/atualização de um registro meteorológico")
public record RegistroMeteorologicoRequestDTO(

        @Schema(description = "Nome da estação meteorológica", example = "Estação Mirante de Santana")
        @NotBlank(message = "O nome da estação é obrigatório")
        @Size(min = 3, max = 100, message = "O nome da estação deve ter entre 3 e 100 caracteres")
        String estacaoNome,

        @Schema(description = "Data do registro (não pode ser futura)", example = "2026-10-14")
        @NotNull(message = "A data do registro é obrigatória")
        @PastOrPresent(message = "A data do registro deve ser menor ou igual à data atual")
        LocalDate dataRegistro,

        @Schema(description = "Temperatura em °C", example = "24.50")
        @NotNull(message = "A temperatura é obrigatória")
        BigDecimal temperatura,

        @Schema(description = "Percentual de umidade relativa do ar (0 a 100)", example = "65.00")
        @NotNull(message = "O percentual de umidade é obrigatório")
        @DecimalMin(value = "0", message = "O percentual de umidade deve ser no mínimo 0")
        @DecimalMax(value = "100", message = "O percentual de umidade deve ser no máximo 100")
        BigDecimal percentualUmidade,

        @Schema(description = "Precipitação em mm (maior ou igual a 0)", example = "0.00")
        @NotNull(message = "A precipitação é obrigatória")
        @DecimalMin(value = "0", message = "A precipitação deve ser maior ou igual a 0")
        BigDecimal precipitacao,

        @Schema(description = "Condição climática", example = "ENSOLARADO")
        @NotNull(message = "A condição climática é obrigatória")
        CondicaoClimatica condicaoClimatica
) {
}
