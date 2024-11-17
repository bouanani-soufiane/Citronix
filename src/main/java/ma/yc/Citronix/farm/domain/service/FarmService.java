package ma.yc.Citronix.farm.domain.service;

import ma.yc.Citronix.common.application.service.CrudService;
import ma.yc.Citronix.farm.application.dto.request.FarmRequestDto;
import ma.yc.Citronix.farm.application.dto.response.FarmResponseDto;
import ma.yc.Citronix.farm.domain.model.aggregate.Farm;
import ma.yc.Citronix.farm.domain.model.valueObject.FarmId;

import java.time.LocalDateTime;
import java.util.List;

public interface FarmService extends CrudService<FarmId, FarmRequestDto, FarmResponseDto> {
    Farm findEntityById(FarmId id);
    List<FarmResponseDto> search( String name, String localization, Double surface, LocalDateTime creationDate );

}
