package ma.yc.Citronix.farm.domain.service;

import ma.yc.Citronix.common.application.service.CrudService;
import ma.yc.Citronix.farm.application.dto.request.FieldRequestDto;
import ma.yc.Citronix.farm.application.dto.request.FieldUpdateDto;
import ma.yc.Citronix.farm.application.dto.response.FieldResponseDto;
import ma.yc.Citronix.farm.domain.model.entity.Field;

public interface FieldService extends CrudService<Field, FieldRequestDto, FieldUpdateDto, FieldResponseDto> {
}