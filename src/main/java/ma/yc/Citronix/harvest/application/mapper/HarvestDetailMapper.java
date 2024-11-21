package ma.yc.Citronix.harvest.application.mapper;

import ma.yc.Citronix.common.application.mapper.BaseMapper;
import ma.yc.Citronix.harvest.application.dto.response.HarvestDetailResponseDto;
import ma.yc.Citronix.harvest.domain.model.aggregate.HarvestDetail;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapper.class)
public interface HarvestDetailMapper {
    HarvestDetailResponseDto toResponseDto( HarvestDetail harvest);

}
