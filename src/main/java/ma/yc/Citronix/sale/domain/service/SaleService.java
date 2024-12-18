package ma.yc.Citronix.sale.domain.service;

import ma.yc.Citronix.common.application.service.CrudService;
import ma.yc.Citronix.sale.application.dto.request.SaleRequestDto;
import ma.yc.Citronix.sale.application.dto.request.SaleUpdateDto;
import ma.yc.Citronix.sale.application.dto.response.SaleResponseDto;
import ma.yc.Citronix.sale.domain.model.valueObject.SaleId;

public interface SaleService extends CrudService<SaleId, SaleRequestDto, SaleUpdateDto, SaleResponseDto> {
}
