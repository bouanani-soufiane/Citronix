package ma.yc.Citronix.farm.application.dto.request.update;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import ma.yc.Citronix.common.application.validation.EntityExists;
import ma.yc.Citronix.farm.domain.model.aggregate.Farm;
import ma.yc.Citronix.farm.domain.model.valueObject.FarmId;

public record FieldUpdateDto(

        @NotBlank String name,

        @Positive @NotNull Double surface,

        @EntityExists(entity = Farm.class)
        @NotNull
        FarmId farm
) {
}