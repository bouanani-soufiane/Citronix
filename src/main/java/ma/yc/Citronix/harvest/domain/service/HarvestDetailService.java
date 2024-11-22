package ma.yc.Citronix.harvest.domain.service;


import ma.yc.Citronix.harvest.application.dto.request.create.HarvestDetailRequestDto;
import ma.yc.Citronix.harvest.application.dto.response.HarvestDetailResponseDto;
import ma.yc.Citronix.harvest.domain.model.valueObject.HarvestDetailId;

import java.util.List;

public interface HarvestDetailService {

    HarvestDetailResponseDto create( HarvestDetailId id, HarvestDetailRequestDto dto);
    HarvestDetailResponseDto findById(HarvestDetailId id);


    HarvestDetailResponseDto update(HarvestDetailId id, HarvestDetailRequestDto dto);

    void delete(HarvestDetailId id);
    List<Object[]> getTotalQuantityByField();
}
