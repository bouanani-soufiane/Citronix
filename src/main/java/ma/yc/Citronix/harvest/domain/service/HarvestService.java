package ma.yc.Citronix.harvest.domain.service;

import ma.yc.Citronix.common.application.service.CrudService;
import ma.yc.Citronix.farm.domain.model.aggregate.Farm;
import ma.yc.Citronix.farm.domain.model.valueObject.FarmId;
import ma.yc.Citronix.harvest.application.dto.request.create.HarvestRequestDto;
import ma.yc.Citronix.harvest.application.dto.request.update.HarvestUpdateDto;
import ma.yc.Citronix.harvest.application.dto.response.HarvestResponseDto;
import ma.yc.Citronix.harvest.domain.model.aggregate.Harvest;
import ma.yc.Citronix.harvest.domain.model.valueObject.HarvestId;

import java.util.List;

public interface HarvestService extends CrudService<HarvestId, HarvestRequestDto, HarvestUpdateDto, HarvestResponseDto> {
    Harvest findEntityById ( HarvestId id );
    Double getTotalHarvestByFarmId(Long farmId);
    Double findByFarm ( FarmId farmId);

}
