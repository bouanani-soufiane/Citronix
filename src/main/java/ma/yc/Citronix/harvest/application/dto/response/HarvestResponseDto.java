package ma.yc.Citronix.harvest.application.dto.response;

import ma.yc.Citronix.harvest.domain.model.enums.Season;
import ma.yc.Citronix.harvest.domain.model.valueObject.HarvestId;

import java.time.LocalDateTime;

public record HarvestResponseDto(
        HarvestId id,
        Season season,
        LocalDateTime date
) {
}
