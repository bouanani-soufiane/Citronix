package ma.yc.Citronix.sale.application.dto.response;

import ma.yc.Citronix.harvest.application.dto.response.HarvestResponseDto;
import ma.yc.Citronix.sale.domain.model.valueObject.SaleId;

import java.time.LocalDate;

public record SaleResponseDto(
        SaleId id,
        LocalDate date,
        String client,
        Double unitPrice,
        Double income,
        HarvestResponseDto harvest
) {
}
