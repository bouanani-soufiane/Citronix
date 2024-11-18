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

    private final FieldMapper mapper;
    private final FieldRepository repository;
    private final FarmService farmService;

    @Override
    public Page<FieldResponseDto> findAll ( int pageNum, int pageSize ) {
        return repository.findAll(PageRequest.of(pageNum, pageSize)).map(mapper::toResponseDto);

    }

    @Override
    public FieldResponseDto findById ( FieldId id ) {
        return mapper.toResponseDto(repository.findById(id).orElseThrow(() -> new NotFoundException("Field", id.value())));

    }

    @Override
    public FieldResponseDto create ( FieldRequestDto dto ) {
        Farm farm = farmService.findEntityById(dto.farm());

        if (farm.getFields().stream().count() >= 10) {
            throw new EntityConstraintViolationException("Farm", "Fields", farm.getFields().stream().count(), "A farm cannot have more than 10 fields.");
        }

        if (dto.surface() < 0.1) {
            throw new EntityConstraintViolationException("Field", "Surface", dto.surface(), "Field surface must be at least 0.1 hectare (1,000 m²).");
        }

        if (dto.surface() > farm.getSurface() * 0.5) {
            throw new EntityConstraintViolationException("Field", "Surface", dto.surface(), "Field surface cannot exceed 50% of the total farm surface (50% of " + farm.getSurface() + " hectares).");
        }

        double totalFieldSurface = farm.getFields().stream().map(Field::getSurface).mapToDouble(Double::doubleValue).sum();

        if (farm.getSurface() < totalFieldSurface + dto.surface()) {
            throw new EntityConstraintViolationException("Farm", "Surface", totalFieldSurface + dto.surface(), "The total surface of all fields cannot exceed the farm's surface (" + farm.getSurface() + " hectares).");
        }

        Field field = mapper.toEntity(dto);
        field.setFarm(farm);

        return mapper.toResponseDto(repository.save(field));
    }


    @Override
    public FieldResponseDto update(FieldId id, FieldUpdateDto dto) {
        Field existingField = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Field", id.value()));

        Farm farm = existingField.getFarm();

        if (dto.surface() < 0.1) {
            throw new EntityConstraintViolationException("Field", "Surface", dto.surface(),
                    "Field surface must be at least 0.1 hectare (1,000 m²).");
        }

        if (dto.surface() > farm.getSurface() * 0.5) {
            throw new EntityConstraintViolationException("Field", "Surface", dto.surface(),
                    "Field surface cannot exceed 50% of the total farm surface (50% of " + farm.getSurface() + " hectares).");
        }

        double totalFieldSurfaceExcludingCurrent = farm.getFields().stream()
                .filter(field -> !field.getId().equals(id))
                .mapToDouble(Field::getSurface)
                .sum();

        if (farm.getSurface() < totalFieldSurfaceExcludingCurrent + dto.surface()) {
            throw new EntityConstraintViolationException("Farm", "Surface", totalFieldSurfaceExcludingCurrent + dto.surface(),
                    "The total surface of all fields cannot exceed the farm's surface (" + farm.getSurface() + " hectares).");
        }

        existingField.setName(dto.name());
        existingField.setSurface(dto.surface());

        Field updatedField = repository.save(existingField);

        return mapper.toResponseDto(updatedField);
    }

    @Override
    public void delete ( FieldId id ) {
        if (!repository.existsById(id)) throw new NotFoundException("Field", id.value());
        repository.deleteById(id);
    }

}
