package ma.yc.Citronix.farm.application.dto.request.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record FarmRequestDto(
        @NotBlank String name,
        @NotBlank String localization,
        @Positive double surface,
        @NotNull
        @PastOrPresent LocalDateTime creationDate
) {
}