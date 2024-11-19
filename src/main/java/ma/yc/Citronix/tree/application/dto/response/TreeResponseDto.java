package ma.yc.Citronix.tree.application.dto.response;

import ma.yc.Citronix.farm.application.dto.response.FieldResponseDto;
import ma.yc.Citronix.tree.domain.model.valueObject.TreeId;

import java.time.LocalDateTime;

public record TreeResponseDto(TreeId id, LocalDateTime plantingDate, FieldResponseDto field) {
}
