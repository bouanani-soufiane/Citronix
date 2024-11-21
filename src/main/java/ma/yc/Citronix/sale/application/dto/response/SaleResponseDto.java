package ma.yc.Citronix.sale.application.dto.response;

import ma.yc.Citronix.sale.domain.model.valueObject.SaleId;

import java.time.LocalDate;

public record SaleResponseDto(
        SaleId id,
        LocalDate date,
        String client,
        Double unitPrice)
{
}
