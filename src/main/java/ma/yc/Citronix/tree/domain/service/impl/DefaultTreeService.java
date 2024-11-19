package ma.yc.Citronix.tree.domain.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.yc.Citronix.common.domain.exception.EntityConstraintViolationException;
import ma.yc.Citronix.common.domain.exception.NotFoundException;
import ma.yc.Citronix.farm.domain.model.entity.Field;
import ma.yc.Citronix.farm.domain.service.FieldService;
import ma.yc.Citronix.tree.application.dto.request.create.TreeRequestDto;
import ma.yc.Citronix.tree.application.dto.request.update.TreeUpdateDto;
import ma.yc.Citronix.tree.application.dto.response.TreeResponseDto;
import ma.yc.Citronix.tree.application.mapper.TreeMapper;
import ma.yc.Citronix.tree.domain.model.aggregate.Tree;
import ma.yc.Citronix.tree.domain.model.valueObject.TreeId;
import ma.yc.Citronix.tree.domain.service.TreeService;
import ma.yc.Citronix.tree.infrastructure.repository.TreeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DefaultTreeService implements TreeService {

    private final TreeRepository repository;
    private final TreeMapper mapper;
    private final FieldService fieldService;

    @Override
    public Page<TreeResponseDto> findAll(int pageNum, int pageSize) {
        return repository.findAll(PageRequest.of(pageNum, pageSize))
                .map(mapper::toResponseDto);
    }

    @Override
    public TreeResponseDto findById(TreeId id) {
        return mapper.toResponseDto(findTreeById(id));
    }
    private Tree findTreeById(TreeId id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tree", id.value()));
    }

    @Override
    public TreeResponseDto create ( TreeRequestDto dto ) {

        if (dto.plantingDate().getMonthValue() < 3 || dto.plantingDate().getMonthValue() > 5) {
            throw new EntityConstraintViolationException("Tree", "planting date", dto.plantingDate().getMonth(), "Trees can only be planted between March and May of any year.");
        }

        Field field = fieldService.findEntityById(dto.field());
        int countTree = repository.countByField(field);

        if ((double) (countTree + 1) / field.getSurface() > 100)
            throw new EntityConstraintViolationException("Tree", "count", countTree, "Cannot add more trees because you have reached the maximum density.");


        Tree tree = mapper.toEntity(dto);
        tree.setField(field);
        repository.save(tree);
        return mapper.toResponseDto(tree);
    }

    @Override
    public TreeResponseDto update ( TreeId id, TreeUpdateDto dto ) {
        return null;
    }

    @Override
    public void delete(TreeId id) {

        if (!repository.existsById(id)) {
            throw new NotFoundException("Tree", id.value());
        }

        repository.deleteById(id);
    }
}
