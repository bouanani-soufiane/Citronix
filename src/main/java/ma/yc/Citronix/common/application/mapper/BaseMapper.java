package ma.yc.Citronix.common.application.mapper;

import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;

@MapperConfig(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {IdValueObjectMapper.class}
)
public interface BaseMapper<ENTITY, REQUEST, RESPONSE> {
    ENTITY toEntity ( REQUEST dto );

    RESPONSE toResponseDto ( ENTITY entity );
}