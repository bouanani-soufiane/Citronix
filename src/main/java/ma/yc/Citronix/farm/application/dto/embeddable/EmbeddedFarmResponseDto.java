package ma.yc.Citronix.farm.application.dto.embeddable;

import java.time.LocalDateTime;

public record EmbeddedFarmResponseDto (
        Long id,
        String name,
        String localization,
        double surface,
        LocalDateTime creationDate
){
}
