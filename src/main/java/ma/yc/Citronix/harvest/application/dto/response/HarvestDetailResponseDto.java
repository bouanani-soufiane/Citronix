package ma.yc.Citronix.harvest.application.dto.response;

import jakarta.validation.constraints.NotNull;
import ma.yc.Citronix.tree.application.dto.response.TreeResponseDto;

import java.time.LocalDate;

public record HarvestDetailResponseDto(@NotNull Double quantity,
                                       @NotNull LocalDate date,
                                       @NotNull TreeResponseDto tree,
                                       @NotNull HarvestResponseDto harvest) {

}