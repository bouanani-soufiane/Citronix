package ma.yc.Citronix.sale.application.mapper;

import ma.yc.Citronix.common.application.mapper.BaseMapper;
import ma.yc.Citronix.sale.application.dto.request.SaleRequestDto;
import ma.yc.Citronix.sale.application.dto.response.SaleResponseDto;
import ma.yc.Citronix.sale.domain.model.aggregate.Sale;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapper.class)
public interface SaleMapper extends BaseMapper<Sale, SaleRequestDto, SaleResponseDto> {
}
