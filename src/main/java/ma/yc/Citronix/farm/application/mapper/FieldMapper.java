package ma.yc.Citronix.farm.application.mapper;

import ma.yc.Citronix.common.application.mapper.BaseMapper;
import ma.yc.Citronix.farm.application.dto.request.FieldRequestDto;
import ma.yc.Citronix.farm.application.dto.response.FieldResponseDto;
import ma.yc.Citronix.farm.domain.model.entity.Field;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapper.class)
public interface FieldMapper extends BaseMapper<Field, FieldRequestDto, FieldResponseDto> {
}
