package ma.yc.Citronix.harvest.application.mapper;

import ma.yc.Citronix.common.application.mapper.BaseMapper;
import ma.yc.Citronix.harvest.application.dto.request.create.HarvestRequestDto;
import ma.yc.Citronix.harvest.application.dto.response.HarvestResponseDto;
import ma.yc.Citronix.harvest.domain.model.aggregate.Harvest;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapper.class)
public interface HarvestMapper extends BaseMapper<Harvest, HarvestRequestDto, HarvestResponseDto> {
}