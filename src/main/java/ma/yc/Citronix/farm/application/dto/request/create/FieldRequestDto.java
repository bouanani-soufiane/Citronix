package ma.yc.Citronix.farm.application.dto.request.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import ma.yc.Citronix.common.application.validation.EntityExists;
import ma.yc.Citronix.common.application.validation.UniqueValue;
import ma.yc.Citronix.farm.domain.model.aggregate.Farm;
import ma.yc.Citronix.farm.domain.model.entity.Field;
import ma.yc.Citronix.farm.domain.model.valueObject.FarmId;

public record FieldRequestDto(

        @NotBlank
        @UniqueValue(fieldName = "name", entityClass = Field.class, message = "field name should be unique")
        String name,

        @Positive @NotNull Double surface,

        @EntityExists(entity = Farm.class)
        @NotNull
        FarmId farm

) {
}