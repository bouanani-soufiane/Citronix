package ma.yc.Citronix.harvest.application.dto.response;

import ma.yc.Citronix.harvest.domain.model.enums.Season;
import ma.yc.Citronix.harvest.domain.model.valueObject.HarvestId;

import java.time.LocalDate;

public record HarvestResponseDto(
        HarvestId id,
        Season season,
        LocalDate date,
        Double totalQuantity
) {
}
