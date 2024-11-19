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

import java.time.Month;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DefaultTreeService implements TreeService {

    private final TreeRepository repository;
    private final TreeMapper mapper;
    private final FieldService fieldService;

    private static final double MAX_TREES_PER_HECTARE = 100.0;
    private static final Month PLANTING_START_MONTH = Month.MARCH;
    private static final Month PLANTING_END_MONTH = Month.MAY;


    @Override
    public Page<TreeResponseDto> findAll ( int pageNum, int pageSize ) {
        return repository.findAll(PageRequest.of(pageNum, pageSize))
                .map(mapper::toResponseDto);
    }

    @Override
    public TreeResponseDto findById ( TreeId id ) {
        return mapper.toResponseDto(findTreeById(id));
    }

    private Tree findTreeById ( TreeId id ) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tree", id.value()));
    }

    @Override
    public TreeResponseDto create ( TreeRequestDto dto ) {

        validateCreateRequest(dto);
        Field field = fieldService.findEntityById(dto.field());
        validateTreeDensity(field);


        Tree tree = mapper.toEntity(dto);
        tree.setField(field);
        repository.save(tree);
        return mapper.toResponseDto(tree);
    }

    @Override
    public TreeResponseDto update(TreeId id, TreeUpdateDto dto) {
        Tree existingTree = findTreeById(id);

        if (dto.field() != null && !dto.field().equals(existingTree.getField().getId())) {
            Field newField = fieldService.findEntityById(dto.field());
            validateTreeDensity(newField);
            existingTree.setField(newField);
        }

        if (dto.plantingDate() != null) {
            Month plantingMonth = dto.plantingDate().getMonth();
            if (plantingMonth.getValue() < PLANTING_START_MONTH.getValue()
                    || plantingMonth.getValue() > PLANTING_END_MONTH.getValue()) {
                throw new EntityConstraintViolationException(
                        "Tree",
                        "planting date",
                        plantingMonth,
                        String.format("Trees can only be planted between %s and %s of any year.",
                                PLANTING_START_MONTH, PLANTING_END_MONTH)
                );
            }
            existingTree.setPlantingDate(dto.plantingDate());
        }

        Tree updatedTree = repository.save(existingTree);
        return mapper.toResponseDto(updatedTree);
    }

    @Override
    public void delete ( TreeId id ) {

        if (!repository.existsById(id)) {
            throw new NotFoundException("Tree", id.value());
        }

        repository.deleteById(id);
    }

    private void validateTreeDensity ( Field field ) {
        int currentTreeCount = repository.countByField(field);
        double newDensity = (double) (currentTreeCount + 1) / field.getSurface();

        if (newDensity > MAX_TREES_PER_HECTARE) {
            throw new EntityConstraintViolationException(
                    "Tree",
                    "density",
                    newDensity,
                    String.format("Cannot add more trees. Maximum density is %.0f trees/ha. " +
                                    "Current density with new tree would be %.2f trees/ha.",
                            MAX_TREES_PER_HECTARE, newDensity)
            );
        }
    }

    private void validateCreateRequest ( TreeRequestDto dto ) {

        Month plantingMonth = dto.plantingDate().getMonth();
        if (plantingMonth.getValue() < PLANTING_START_MONTH.getValue()
                || plantingMonth.getValue() > PLANTING_END_MONTH.getValue()) {
            throw new EntityConstraintViolationException(
                    "Tree",
                    "planting date",
                    plantingMonth,
                    String.format("Trees can only be planted between %s and %s of any year.",
                            PLANTING_START_MONTH, PLANTING_END_MONTH)
            );
        }
    }
}
