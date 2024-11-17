package ma.yc.Citronix.farm.application.dto.response;

import java.time.LocalDateTime;

public record FarmResponseDto(
        Long id,
        String name,
        String localization,
        double surface,
        LocalDateTime creationDate
) {
}
