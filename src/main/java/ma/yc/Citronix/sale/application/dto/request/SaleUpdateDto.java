package ma.yc.Citronix.sale.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record SaleUpdateDto(
        @NotNull LocalDate date,

        @NotBlank String client,

        @Positive Double unitPrice

) {
}
