package ma.yc.Citronix.common.application.mapper;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IdValueObjectMapper {

    default Long valueObjectToUuid ( Object vo ) {
        if (vo == null) {
            return null;
        }
        try {
            return (Long) vo.getClass().getMethod("value").invoke(vo);
        } catch (Exception e) {
            return null;
        }
    }
}