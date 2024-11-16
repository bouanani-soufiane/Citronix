package ma.yc.Citronix.farm.domain.service;

import ma.yc.Citronix.common.application.service.CrudService;
import ma.yc.Citronix.farm.application.dto.request.FarmRequestDto;
import ma.yc.Citronix.farm.application.dto.response.FarmResponseDto;

public interface FarmService extends CrudService<Long, FarmRequestDto, FarmResponseDto> {
}
