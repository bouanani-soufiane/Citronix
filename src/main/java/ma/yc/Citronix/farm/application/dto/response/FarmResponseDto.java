package ma.yc.Citronix.farm.application.dto.response;

import ma.yc.Citronix.farm.application.dto.embeddable.EmbeddedFieldResponseDto;

import java.time.LocalDate;
import java.util.List;

public record FarmResponseDto(
        Long id,
        String name,
        String localization,
        double surface,
        LocalDate creationDate,
        List<EmbeddedFieldResponseDto> fields
) {
}
