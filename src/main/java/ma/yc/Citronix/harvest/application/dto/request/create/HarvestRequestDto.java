package ma.yc.Citronix.harvest.application.dto.request.create;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import ma.yc.Citronix.common.application.validation.EntityExists;
import ma.yc.Citronix.farm.domain.model.aggregate.Farm;
import ma.yc.Citronix.farm.domain.model.valueObject.FarmId;
import ma.yc.Citronix.harvest.domain.model.enums.Season;

import java.time.LocalDate;

public record HarvestRequestDto(

        @NotNull Season season,

        @NotNull @PastOrPresent LocalDate date,

        @EntityExists(entity = Farm.class) FarmId farm

)
{
}
