package ma.yc.Citronix.harvest.domain.service;


import ma.yc.Citronix.harvest.application.dto.request.create.HarvestDetailRequestDto;
import ma.yc.Citronix.harvest.application.dto.response.HarvestDetailResponseDto;
import ma.yc.Citronix.harvest.domain.model.valueObject.HarvestDetailId;

public interface HarvestDetailService {

    HarvestDetailResponseDto create( HarvestDetailId id, HarvestDetailRequestDto dto);

}
