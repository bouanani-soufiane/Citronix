package ma.yc.Citronix.farm.domain.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.yc.Citronix.common.domain.exception.EntityConstraintViolationException;
import ma.yc.Citronix.common.domain.exception.NotFoundException;
import ma.yc.Citronix.farm.application.dto.request.create.FarmRequestDto;
import ma.yc.Citronix.farm.application.dto.request.update.FarmUpdateDto;
import ma.yc.Citronix.farm.application.dto.response.FarmResponseDto;
import ma.yc.Citronix.farm.application.mapper.FarmMapper;
import ma.yc.Citronix.farm.domain.model.aggregate.Farm;
import ma.yc.Citronix.farm.domain.model.valueObject.FarmId;
import ma.yc.Citronix.farm.domain.service.FarmService;
import ma.yc.Citronix.farm.infrastructure.repository.FarmRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DefaultFarmService implements FarmService {
    private final FarmRepository repository;
    private final FarmMapper mapper;

    @Override
    public Page<FarmResponseDto> findAll ( int pageNum, int pageSize ) {
        return repository.findAll(PageRequest.of(pageNum, pageSize)).map(mapper::toResponseDto);
    }

    @Override
    public FarmResponseDto findById ( FarmId id ) {
        return mapper.toResponseDto(repository.findById(id).orElseThrow(() -> new NotFoundException("Farm", id.value())));
    }

    @Override
    public Farm findEntityById ( FarmId id ) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("farm", id.value()));
    }

    @Override
    public List<FarmResponseDto> search ( String name, String localization, Double surface, LocalDate creationDate ) {
        List<Farm> farms = repository.search(name, localization, surface, creationDate);
        return farms.stream().map(mapper::toResponseDto).toList();
    }

    @Override
    public FarmResponseDto create ( FarmRequestDto dto ) {
        if (dto.surface() < 0.2) {
            throw new EntityConstraintViolationException("Farm", "Surface", dto.surface(), "Farm surface must be at least 0.2 hectare (2,000 m²).");
        }

        Farm farm = Farm.builder()
                .name(dto.name())
                .localization(dto.localization())
                .surface(dto.surface())
                .fields(List.of())
                .creationDate(dto.creationDate())
                .build();

        return mapper.toResponseDto(repository.save(farm));
    }


    @Override
    public FarmResponseDto update ( FarmId id, FarmUpdateDto dto ) {
        if (dto.surface() < 0.2) {
            throw new EntityConstraintViolationException("Farm", "Surface", dto.surface(), "Farm surface must be at least 0.2 hectare (2,000 m²).");
        }

        Farm farm = findEntityById(id);
        if (dto.name() != null && dto.name() != "") {
            farm.setName(dto.name());
        }
        if (dto.localization() != null && dto.localization() != "") {
            farm.setLocalization(dto.localization());
        }
        if (dto.surface() != null) {
            farm.setSurface(dto.surface());
        }
        if (dto.creationDate() != null) {
            farm.setCreationDate(dto.creationDate());
        }
        return mapper.toResponseDto(farm);
    }

    @Override
    public void delete ( FarmId id ) {
        if (!repository.existsById(id)) throw new NotFoundException("Farm", id.value());
        repository.deleteById(id);
    }
}
