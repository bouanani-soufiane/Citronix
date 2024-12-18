package ma.yc.Citronix.harvest.domain.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.yc.Citronix.common.domain.exception.EntityConstraintViolationException;
import ma.yc.Citronix.common.domain.exception.NotFoundException;
import ma.yc.Citronix.farm.domain.model.aggregate.Farm;
import ma.yc.Citronix.farm.domain.model.valueObject.FarmId;
import ma.yc.Citronix.farm.domain.service.FarmService;
import ma.yc.Citronix.harvest.application.dto.request.create.HarvestRequestDto;
import ma.yc.Citronix.harvest.application.dto.request.update.HarvestUpdateDto;
import ma.yc.Citronix.harvest.application.dto.response.HarvestResponseDto;
import ma.yc.Citronix.harvest.application.mapper.HarvestMapper;
import ma.yc.Citronix.harvest.domain.model.aggregate.Harvest;
import ma.yc.Citronix.harvest.domain.model.enums.Season;
import ma.yc.Citronix.harvest.domain.model.valueObject.HarvestId;
import ma.yc.Citronix.harvest.domain.service.HarvestService;
import ma.yc.Citronix.harvest.infrastructure.repository.HarvestRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DefaultHarvestService implements HarvestService {


    private final HarvestRepository repository;
    private final HarvestMapper mapper;
    private final FarmService farmService;

    @Override
    public Page<HarvestResponseDto> findAll ( int pageNum, int pageSize ) {
        return repository.findAll(PageRequest.of(pageNum, pageSize)).map(mapper::toResponseDto);

    }

    @Override
    public HarvestResponseDto findById ( HarvestId id ) {
        return mapper.toResponseDto(repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Harvest", id.value())));

    }

    @Override
    public HarvestResponseDto create ( HarvestRequestDto dto ) {
        Farm farm = farmService.findEntityById(dto.farm());

        validateSeasonDate(dto.season(), dto.date());

        validateNoExistingHarvest(farm.getId(), dto.season(), dto.date());

        Harvest harvest = Harvest.builder().season(dto.season()).date(dto.date()).totalQuantity(0.0)
                .farm(farm).build();

        return mapper.toResponseDto(repository.save(harvest));
    }

    @Override
    public HarvestResponseDto update ( HarvestId id, HarvestUpdateDto dto ) {
        Harvest existingHarvest = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Harvest", id.value()));

        Farm farm = farmService.findEntityById(dto.farm());
        Season season = dto.season();
        LocalDate date = dto.date();

        validateSeasonDate(season, date);

        if (dto.season() != null || dto.date() != null) {
            boolean isSameYearAndSeason = date.getYear() == existingHarvest.getDate().getYear() && season == existingHarvest.getSeason();

            if (!isSameYearAndSeason) {
                validateNoExistingHarvest(farm.getId(), season, date);
            }
        }

        existingHarvest.setSeason(season);
        existingHarvest.setDate(date);
        existingHarvest.setFarm(farm);

        return mapper.toResponseDto(existingHarvest);
    }

    @Override
    public void delete ( HarvestId id ) {
        if (!repository.existsById(id)) throw new NotFoundException("Harvest", id.value());
        repository.deleteById(id);
    }


    private void validateSeasonDate ( Season season, LocalDate date ) {
        int month = date.getMonthValue();
        boolean isValidSeason = switch (season) {
            case SPRING -> month >= 3 && month <= 5;
            case SUMMER -> month >= 6 && month <= 8;
            case AUTUMN -> month >= 9 && month <= 11;
            case WINTER -> month == 12 || month <= 2;
        };

        if (!isValidSeason) {
            throw new EntityConstraintViolationException("Harvest", "season", season, "does not match the provided date.");
        }
    }

    private void validateNoExistingHarvest ( FarmId id, Season season, LocalDate date ) {

        int year = date.getYear();

        boolean exists = repository.existsByFarmIdAndSeasonAndYear(id, season, year);

        if (exists) {
            throw new EntityConstraintViolationException("Harvest", "", season, "already exists for the specified season and year.");

        }
    }
    public Double getTotalHarvestByFarmId(Long farmId) {
        return repository.findTotalHarvestByFarmId(farmId)
                .orElse(0.0);
    }

    @Override
    public Double findByFarm ( FarmId farmId ) {

        List<Harvest> harvests = repository.findByFarm(farmId);

        double sum = harvests.stream().mapToDouble(harvest -> harvest.getTotalQuantity()).sum();
        return sum;
    }

    @Override
    public Harvest findEntityById ( HarvestId id ) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Harvest", id));
    }
}

