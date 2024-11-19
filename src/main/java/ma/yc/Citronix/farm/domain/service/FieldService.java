package ma.yc.Citronix.farm.domain.service;

import ma.yc.Citronix.common.application.service.CrudService;
import ma.yc.Citronix.farm.application.dto.request.create.FieldRequestDto;
import ma.yc.Citronix.farm.application.dto.request.update.FieldUpdateDto;
import ma.yc.Citronix.farm.application.dto.response.FieldResponseDto;
import ma.yc.Citronix.farm.domain.model.entity.Field;
import ma.yc.Citronix.farm.domain.model.valueObject.FieldId;

public interface FieldService extends CrudService<FieldId, FieldRequestDto, FieldUpdateDto, FieldResponseDto> {
    Field findEntityById (FieldId id);
}
