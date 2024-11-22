package ma.yc.Citronix.harvest;

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
import ma.yc.Citronix.harvest.domain.service.impl.DefaultHarvestService;
import ma.yc.Citronix.harvest.infrastructure.repository.HarvestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class HarvestServiceTest {
    private static final Long HARVEST_ID = 1L;
    private static final Long FARM_ID = 1L;
    private static final Season SEASON = Season.SUMMER;
    private static final LocalDate VALID_SUMMER_DATE = LocalDate.of(2024, 7, 15);
    private static final double TOTAL_QUANTITY = 1000.0;

    @Mock
    private HarvestRepository harvestRepository;

    @Mock
    private HarvestMapper harvestMapper;

    @Mock
    private FarmService farmService;

    private DefaultHarvestService harvestService;
    private Harvest testHarvest;
    private HarvestId testHarvestId;
    private Farm testFarm;
    private FarmId testFarmId;

    @BeforeEach
    void setUp() {
        harvestService = new DefaultHarvestService(harvestRepository, harvestMapper, farmService);
        testHarvestId = new HarvestId(HARVEST_ID);
        testFarmId = new FarmId(FARM_ID);
        testFarm = createTestFarm();
        testHarvest = createTestHarvest();
    }

    private Farm createTestFarm() {
        return Farm.builder()
                .id(testFarmId)
                .build();
    }

    private Harvest createTestHarvest() {
        return Harvest.builder()
                .id(testHarvestId)
                .season(SEASON)
                .date(VALID_SUMMER_DATE)
                .totalQuantity(TOTAL_QUANTITY)
                .farm(testFarm)
                .build();
    }

    private HarvestResponseDto createExpectedResponse(Harvest harvest) {
        return new HarvestResponseDto(
                harvest.getId(),
                harvest.getSeason(),
                harvest.getDate(),
                harvest.getTotalQuantity()
        );
    }

    @Nested
    class FindAllTests {
        @Test
        void shouldReturnEmptyPage_WhenNoHarvestsExist() {
            given(harvestRepository.findAll(any(PageRequest.class)))
                    .willReturn(new PageImpl<>(Collections.emptyList()));

            Page<HarvestResponseDto> result = harvestService.findAll(0, 10);

            assertThat(result).isEmpty();
        }

        @Test
        void shouldReturnPageOfHarvests_WhenHarvestsExist() {
            given(harvestRepository.findAll(any(PageRequest.class)))
                    .willReturn(new PageImpl<>(List.of(testHarvest)));
            given(harvestMapper.toResponseDto(testHarvest))
                    .willReturn(createExpectedResponse(testHarvest));

            Page<HarvestResponseDto> result = harvestService.findAll(0, 10);

            assertThat(result).hasSize(1);
            assertThat(result.getContent().get(0).season()).isEqualTo(SEASON);
        }
    }

    @Nested
    class CreateTests {
        @Test
        void shouldThrowException_WhenSeasonDoesNotMatchDate() {
            HarvestRequestDto request = new HarvestRequestDto(
                    Season.WINTER,
                    VALID_SUMMER_DATE,
                    testFarmId
            );

            assertThatExceptionOfType(EntityConstraintViolationException.class)
                    .isThrownBy(() -> harvestService.create(request));
        }

        @Test
        void shouldThrowException_WhenHarvestAlreadyExistsForSeasonAndYear() {
            given(farmService.findEntityById(testFarmId)).willReturn(testFarm);
            given(harvestRepository.existsByFarmIdAndSeasonAndYear(testFarmId, SEASON, VALID_SUMMER_DATE.getYear()))
                    .willReturn(true);

            HarvestRequestDto request = new HarvestRequestDto(SEASON, VALID_SUMMER_DATE, testFarmId);

            assertThatExceptionOfType(EntityConstraintViolationException.class)
                    .isThrownBy(() -> harvestService.create(request));
        }

        @Test
        void shouldCreateHarvest_WhenValidRequest() {
            given(farmService.findEntityById(testFarmId)).willReturn(testFarm);
            given(harvestRepository.existsByFarmIdAndSeasonAndYear(testFarmId, SEASON, VALID_SUMMER_DATE.getYear()))
                    .willReturn(false);
            given(harvestRepository.save(any(Harvest.class))).willReturn(testHarvest);
            given(harvestMapper.toResponseDto(testHarvest)).willReturn(createExpectedResponse(testHarvest));

            HarvestRequestDto request = new HarvestRequestDto(SEASON, VALID_SUMMER_DATE, testFarmId);

            HarvestResponseDto result = harvestService.create(request);

            assertThat(result.season()).isEqualTo(SEASON);
            assertThat(result.date()).isEqualTo(VALID_SUMMER_DATE);
        }
    }

    @Nested
    class UpdateTests {
        @Test
        void shouldThrowNotFoundException_WhenHarvestDoesNotExist() {
            HarvestUpdateDto request = new HarvestUpdateDto(SEASON, VALID_SUMMER_DATE, testFarmId);
            given(harvestRepository.findById(testHarvestId)).willReturn(Optional.empty());

            assertThatExceptionOfType(NotFoundException.class)
                    .isThrownBy(() -> harvestService.update(testHarvestId, request));
        }

        @Test
        void shouldUpdateHarvest_WhenValidRequest() {
            given(harvestRepository.findById(testHarvestId)).willReturn(Optional.of(testHarvest));
            given(farmService.findEntityById(testFarmId)).willReturn(testFarm);
            given(harvestRepository.save(any(Harvest.class))).willReturn(testHarvest);
            given(harvestMapper.toResponseDto(testHarvest)).willReturn(createExpectedResponse(testHarvest));

            HarvestUpdateDto request = new HarvestUpdateDto(SEASON, VALID_SUMMER_DATE, testFarmId);

            HarvestResponseDto result = harvestService.update(testHarvestId, request);

            assertThat(result.season()).isEqualTo(SEASON);
            assertThat(result.date()).isEqualTo(VALID_SUMMER_DATE);
        }
    }

    @Nested
    class DeleteTests {
        @Test
        void shouldDeleteHarvest_WhenHarvestExists() {
            given(harvestRepository.existsById(testHarvestId)).willReturn(true);

            harvestService.delete(testHarvestId);

            verify(harvestRepository).deleteById(testHarvestId);
        }

        @Test
        void shouldThrowNotFoundException_WhenDeletingNonexistentHarvest() {
            given(harvestRepository.existsById(testHarvestId)).willReturn(false);

            assertThatExceptionOfType(NotFoundException.class)
                    .isThrownBy(() -> harvestService.delete(testHarvestId));
        }
    }
}