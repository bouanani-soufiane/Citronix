package ma.yc.Citronix.tree.application.mapper;

import ma.yc.Citronix.common.application.mapper.BaseMapper;
import ma.yc.Citronix.tree.application.dto.request.create.TreeRequestDto;
import ma.yc.Citronix.tree.application.dto.response.TreeResponseDto;
import ma.yc.Citronix.tree.domain.model.aggregate.Tree;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapper.class)
public interface TreeMapper extends BaseMapper<Tree, TreeRequestDto, TreeResponseDto> {
}
