package ma.yc.Citronix.harvest.application.dto.request.update;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import ma.yc.Citronix.common.application.validation.EntityExists;
import ma.yc.Citronix.farm.domain.model.aggregate.Farm;
import ma.yc.Citronix.farm.domain.model.valueObject.FarmId;
import ma.yc.Citronix.harvest.domain.model.enums.Season;

import java.time.LocalDateTime;

public record HarvestUpdateDto(
        @NotNull Season season,

        @NotNull @PastOrPresent LocalDateTime date,

        @EntityExists(entity = Farm.class) FarmId farm

)
{
}
