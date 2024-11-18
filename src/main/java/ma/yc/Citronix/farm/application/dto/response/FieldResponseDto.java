package ma.yc.Citronix.farm.application.dto.response;

import ma.yc.Citronix.farm.domain.model.valueObject.FieldId;

public record FieldResponseDto(
        FieldId id,
        String name,
        double surface,
        FarmResponseDto farm
) {
}
