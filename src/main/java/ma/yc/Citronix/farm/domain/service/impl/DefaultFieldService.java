package ma.yc.Citronix.farm.domain.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.yc.Citronix.common.domain.exception.EntityConstraintViolationException;
import ma.yc.Citronix.common.domain.exception.NotFoundException;
import ma.yc.Citronix.farm.application.dto.request.create.FieldRequestDto;
import ma.yc.Citronix.farm.application.dto.request.update.FieldUpdateDto;
import ma.yc.Citronix.farm.application.dto.response.FieldResponseDto;
import ma.yc.Citronix.farm.application.mapper.FieldMapper;
import ma.yc.Citronix.farm.domain.model.aggregate.Farm;
import ma.yc.Citronix.farm.domain.model.entity.Field;
import ma.yc.Citronix.farm.domain.model.valueObject.FieldId;
import ma.yc.Citronix.farm.domain.service.FarmService;
import ma.yc.Citronix.farm.domain.service.FieldService;
import ma.yc.Citronix.farm.infrastructure.repository.FieldRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DefaultFieldService implements FieldService {
    private static final double MIN_FIELD_SURFACE = 0.1;
    private static final double MAX_FIELD_PERCENTAGE = 0.5;
    private static final int MAX_FIELDS_PER_FARM = 10;

    private final FieldMapper mapper;
    private final FieldRepository repository;
    private final FarmService farmService;

    @Override
    public Page<FieldResponseDto> findAll(int pageNum, int pageSize) {
        return repository.findAll(PageRequest.of(pageNum, pageSize))
                .map(mapper::toResponseDto);
    }

    @Override
    public FieldResponseDto findById(FieldId id) {
        return mapper.toResponseDto(findFieldById(id));
    }

    @Override
    public FieldResponseDto create(FieldRequestDto dto) {
        Farm farm = farmService.findEntityById(dto.farm());
        validateFieldCreation(farm, dto.surface());

        Field field = mapper.toEntity(dto);
        field.setFarm(farm);

        return mapper.toResponseDto(repository.save(field));
    }

    @Override
    public FieldResponseDto update(FieldId id, FieldUpdateDto dto) {
        Field existingField = findFieldById(id);
        Farm newFarm = dto.farm() != null ? farmService.findEntityById(dto.farm()) : existingField.getFarm();

        validateFieldUpdate(newFarm, dto.surface(), id);

        updateFieldProperties(existingField, newFarm, dto);
        return mapper.toResponseDto(existingField);
    }

    @Override
    public void delete(FieldId id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Field", id.value());
        }
        repository.deleteById(id);
    }

    private Field findFieldById(FieldId id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Field", id.value()));
    }

    private void validateFieldCreation(Farm farm, double newFieldSurface) {
        validateFieldCount(farm);
        validateFieldSurface(newFieldSurface);
        validateFieldSurfacePercentage(farm, newFieldSurface);
        validateTotalFarmSurface(farm, newFieldSurface, null);
    }

    private void validateFieldUpdate(Farm farm, double newFieldSurface, FieldId fieldId) {
        validateFieldSurface(newFieldSurface);
        validateFieldSurfacePercentage(farm, newFieldSurface);
        validateTotalFarmSurface(farm, newFieldSurface, fieldId);
    }

    private void validateFieldCount(Farm farm) {
        if (farm.getFields().size() >= MAX_FIELDS_PER_FARM) {
            throw new EntityConstraintViolationException("Farm", "Fields",
                    farm.getFields().size(),
                    String.format("A farm cannot have more than %d fields.", MAX_FIELDS_PER_FARM));
        }
    }

    private void validateFieldSurface(double surface) {
        if (surface < MIN_FIELD_SURFACE) {
            throw new EntityConstraintViolationException("Field", "Surface", surface,
                    String.format("Field surface must be at least %.1f hectare (1,000 m²).", MIN_FIELD_SURFACE));
        }
    }

    private void validateFieldSurfacePercentage(Farm farm, double fieldSurface) {
        if (fieldSurface > farm.getSurface() * MAX_FIELD_PERCENTAGE) {
            throw new EntityConstraintViolationException("Field", "Surface", fieldSurface,
                    String.format("Field surface cannot exceed %.0f%% of the total farm surface (%.0f%% of %.2f hectares).",
                            MAX_FIELD_PERCENTAGE * 100, MAX_FIELD_PERCENTAGE * 100, farm.getSurface()));
        }
    }

    private void validateTotalFarmSurface(Farm farm, double newFieldSurface, FieldId excludeFieldId) {
        double totalFieldSurface = farm.getFields().stream()
                .filter(field -> excludeFieldId == null || !field.getId().equals(excludeFieldId))
                .mapToDouble(Field::getSurface)
                .sum() + newFieldSurface;

        if (farm.getSurface() < totalFieldSurface) {
            throw new EntityConstraintViolationException("Farm", "Surface", totalFieldSurface,
                    String.format("The total surface of all fields cannot exceed the farm's surface (%.2f hectares).",
                            farm.getSurface()));
        }
    }

    private void updateFieldProperties(Field field, Farm newFarm, FieldUpdateDto dto) {
        if (!field.getFarm().equals(newFarm)) {
            field.setFarm(newFarm);
        }
        field.setName(dto.name());
        field.setSurface(dto.surface());
    }

    @Override
    public Field findEntityById ( FieldId id ) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("field", id.value()));

    }
}