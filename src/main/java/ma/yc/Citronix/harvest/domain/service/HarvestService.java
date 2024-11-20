package ma.yc.Citronix.harvest.domain.service;

import ma.yc.Citronix.common.application.service.CrudService;
import ma.yc.Citronix.harvest.application.dto.request.create.HarvestRequestDto;
import ma.yc.Citronix.harvest.application.dto.request.update.HarvestUpdateDto;
import ma.yc.Citronix.harvest.application.dto.response.HarvestResponseDto;
import ma.yc.Citronix.harvest.domain.model.valueObject.HarvestId;

public interface HarvestService extends CrudService<HarvestId, HarvestRequestDto, HarvestUpdateDto , HarvestResponseDto> {
}
