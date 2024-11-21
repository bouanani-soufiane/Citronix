package ma.yc.Citronix.tree.application.dto.request.create;

import jakarta.validation.constraints.NotNull;
import ma.yc.Citronix.common.application.validation.EntityExists;
import ma.yc.Citronix.farm.domain.model.entity.Field;
import ma.yc.Citronix.farm.domain.model.valueObject.FieldId;

import java.time.LocalDate;

public record TreeRequestDto(
        @NotNull LocalDate plantingDate,
        @NotNull @EntityExists(entity = Field.class) FieldId field
) {
}
