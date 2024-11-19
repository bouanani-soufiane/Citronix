package ma.yc.Citronix.tree.domain.service;

import ma.yc.Citronix.common.application.service.CrudService;
import ma.yc.Citronix.tree.application.dto.request.create.TreeRequestDto;
import ma.yc.Citronix.tree.application.dto.request.update.TreeUpdateDto;
import ma.yc.Citronix.tree.application.dto.response.TreeResponseDto;
import ma.yc.Citronix.tree.domain.model.valueObject.TreeId;

public interface TreeService extends CrudService<TreeId, TreeRequestDto, TreeUpdateDto, TreeResponseDto> {
}
