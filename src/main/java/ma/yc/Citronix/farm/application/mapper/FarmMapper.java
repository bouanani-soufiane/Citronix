package ma.yc.Citronix.farm.application.mapper;

import ma.yc.Citronix.common.application.mapper.BaseMapper;
import ma.yc.Citronix.farm.application.dto.request.FarmRequestDto;
import ma.yc.Citronix.farm.application.dto.response.FarmResponseDto;
import ma.yc.Citronix.farm.domain.model.aggregate.Farm;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapper.class)
public interface FarmMapper extends BaseMapper<Farm, FarmRequestDto , FarmResponseDto>{
}
