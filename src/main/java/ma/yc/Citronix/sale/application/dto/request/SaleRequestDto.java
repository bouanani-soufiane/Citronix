package ma.yc.Citronix.sale.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import ma.yc.Citronix.common.application.validation.EntityExists;
import ma.yc.Citronix.harvest.domain.model.aggregate.Harvest;
import ma.yc.Citronix.harvest.domain.model.valueObject.HarvestId;

import java.time.LocalDate;

public record SaleRequestDto(
        @NotNull LocalDate date,

        @NotBlank String client,

        @Positive Double unitPrice,

        @EntityExists(entity = Harvest.class) HarvestId harvest

) {
}
