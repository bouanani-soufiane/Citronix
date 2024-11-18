package ma.yc.Citronix.farm.application.dto.embeddable;

import ma.yc.Citronix.farm.domain.model.valueObject.FieldId;

public record EmbeddedFieldResponseDto(
        FieldId id,
        String name,
        double surface
) {
}
