package ma.yc.Citronix.harvest.application.dto.request.update;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import ma.yc.Citronix.harvest.domain.model.enums.Season;

import java.time.LocalDateTime;

public record HarvestUpdateDto(
        @NotNull Season name,

        @NotNull @PastOrPresent LocalDateTime date)
{
}
