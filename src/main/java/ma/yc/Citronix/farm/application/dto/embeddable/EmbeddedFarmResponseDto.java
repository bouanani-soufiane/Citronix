package ma.yc.Citronix.farm.application.dto.embeddable;

import java.time.LocalDate;

public record EmbeddedFarmResponseDto(
        Long id,
        String name,
        String localization,
        double surface,
        LocalDate creationDate
) {
}
