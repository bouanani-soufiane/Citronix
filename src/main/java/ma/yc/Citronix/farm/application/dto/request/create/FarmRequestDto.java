package ma.yc.Citronix.farm.application.dto.request.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import ma.yc.Citronix.common.application.validation.UniqueValue;
import ma.yc.Citronix.farm.domain.model.aggregate.Farm;

import java.time.LocalDate;

public record FarmRequestDto(
        @NotBlank
        @UniqueValue(fieldName = "name" , entityClass = Farm.class , message = "farm name should be unique")
        String name,
        @NotBlank String localization,
        @Positive @NotNull Double surface,
        @NotNull
        @PastOrPresent LocalDate creationDate
) {
}