package ma.yc.Citronix.tree.application.dto.request.update;

import ma.yc.Citronix.common.application.validation.EntityExists;
import ma.yc.Citronix.farm.domain.model.entity.Field;
import ma.yc.Citronix.farm.domain.model.valueObject.FieldId;

import java.time.LocalDateTime;

public record TreeUpdateDto(
        LocalDateTime plantingDate,
        @EntityExists(entity = Field.class) FieldId field
) {
}