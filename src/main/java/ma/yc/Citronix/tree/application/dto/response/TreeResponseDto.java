package ma.yc.Citronix.tree.application.dto.response;

import ma.yc.Citronix.farm.application.dto.response.FieldResponseDto;
import ma.yc.Citronix.tree.domain.model.valueObject.TreeId;

import java.time.LocalDate;

public record TreeResponseDto(
        TreeId id,
        LocalDate plantingDate,
        Integer age,
        Double productivity,
        FieldResponseDto field) {
}
