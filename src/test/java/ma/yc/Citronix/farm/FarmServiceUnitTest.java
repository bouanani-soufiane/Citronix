package ma.yc.Citronix.farm;

import ma.yc.Citronix.common.domain.exception.NotFoundException;
import ma.yc.Citronix.farm.application.dto.request.create.FarmRequestDto;
import ma.yc.Citronix.farm.application.dto.request.update.FarmUpdateDto;
import ma.yc.Citronix.farm.application.dto.response.FarmResponseDto;
import ma.yc.Citronix.farm.application.mapper.FarmMapper;
import ma.yc.Citronix.farm.domain.model.aggregate.Farm;
import ma.yc.Citronix.farm.domain.model.valueObject.FarmId;
import ma.yc.Citronix.farm.domain.service.FarmService;
import ma.yc.Citronix.farm.domain.service.impl.DefaultFarmService;
import ma.yc.Citronix.farm.infrastructure.repository.FarmRepository;
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
class FarmServiceTest {
    private static final Long FARM_ID = 1L;
    private static final String FARM_NAME = "Azura Farm";
    private static final String FARM_LOCATION = "Agadir";
    private static final double FARM_SURFACE = 2000.0;

    @Mock
    private FarmRepository farmRepository;

    @Mock
    private FarmMapper farmMapper;

    private FarmService farmService;
    private Farm testFarm;
    private FarmId testFarmId;

    @BeforeEach
    void setUp() {
        farmService = new DefaultFarmService(farmRepository, farmMapper);
        testFarmId = new FarmId(FARM_ID);
        testFarm = createTestFarm();
    }

    private Farm createTestFarm() {
        return Farm.builder()
                .id(testFarmId)
                .name(FARM_NAME)
                .localization(FARM_LOCATION)
                .surface(FARM_SURFACE)
                .creationDate(LocalDate.now())
                .fields(Collections.emptyList())
                .harvests(Collections.emptyList())
                .build();
    }

    private FarmResponseDto createExpectedResponse(Farm farm) {
        return new FarmResponseDto(
                farm.getId().value(),
                farm.getName(),
                farm.getLocalization(),
                farm.getSurface(),
                farm.getCreationDate(),
                Collections.emptyList()
        );
    }

    @Nested
    class FindAllTests {
        @Test
        void shouldReturnEmptyPage_WhenNoFarmsExist() {
            // Given
            given(farmRepository.findAll(any(PageRequest.class)))
                    .willReturn(new PageImpl<>(Collections.emptyList()));

            // When
            Page<FarmResponseDto> result = farmService.findAll(0, 10);

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        void shouldReturnPageOfFarms_WhenFarmsExist() {
            // Given
            given(farmRepository.findAll(any(PageRequest.class)))
                    .willReturn(new PageImpl<>(List.of(testFarm)));
            given(farmMapper.toResponseDto(testFarm))
                    .willReturn(createExpectedResponse(testFarm));

            // When
            Page<FarmResponseDto> result = farmService.findAll(0, 10);

            // Then
            assertThat(result).hasSize(1);
            assertThat(result.getContent().get(0).name()).isEqualTo(FARM_NAME);
        }
    }

    @Nested
    class FindByIdTests {
        @Test
        void shouldThrowNotFoundException_WhenFarmDoesNotExist() {
            // Given
            given(farmRepository.findById(testFarmId)).willReturn(Optional.empty());

            // When/Then
            assertThatExceptionOfType(NotFoundException.class)
                    .isThrownBy(() -> farmService.findById(testFarmId))
                    .withMessage("Farm with id " + FARM_ID + " not found");
        }

        @Test
        void shouldReturnFarm_WhenFarmExists() {
            // Given
            given(farmRepository.findById(testFarmId)).willReturn(Optional.of(testFarm));
            given(farmMapper.toResponseDto(testFarm)).willReturn(createExpectedResponse(testFarm));

            // When
            FarmResponseDto result = farmService.findById(testFarmId);

            // Then
            assertThat(result.id()).isEqualTo(FARM_ID);
            assertThat(result.name()).isEqualTo(FARM_NAME);
        }
    }

    @Nested
    class CreateTests {
        @Test
        void shouldCreateAndReturnFarm_WhenValidRequest() {
            // Given
            FarmRequestDto request = new FarmRequestDto(FARM_NAME, FARM_LOCATION, FARM_SURFACE, null);
            given(farmMapper.toEntity(request)).willReturn(testFarm);
            given(farmRepository.save(any(Farm.class))).willReturn(testFarm);
            given(farmMapper.toResponseDto(testFarm)).willReturn(createExpectedResponse(testFarm));

            // When
            FarmResponseDto result = farmService.create(request);

            // Then
            assertThat(result.name()).isEqualTo(FARM_NAME);
            assertThat(result.localization()).isEqualTo(FARM_LOCATION);
        }
    }

    @Nested
    class UpdateTests {
        @Test
        void shouldThrowNotFoundException_WhenUpdatingNonexistentFarm() {
            // Given
            FarmUpdateDto request = new FarmUpdateDto("Updated Farm", "New Location", FARM_SURFACE, null);
            given(farmRepository.findById(testFarmId)).willReturn(Optional.empty());

            // When/Then
            assertThatExceptionOfType(NotFoundException.class)
                    .isThrownBy(() -> farmService.update(testFarmId, request))
                    .withMessage("farm with id " + FARM_ID + " not found");
        }

        @Test
        void shouldUpdateAndReturnFarm_WhenValidRequest() {
            // Given
            String updatedName = "Updated Farm";
            String updatedLocation = "New Location";
            FarmUpdateDto request = new FarmUpdateDto(updatedName, updatedLocation, FARM_SURFACE, null);

            given(farmRepository.findById(testFarmId)).willReturn(Optional.of(testFarm));
            given(farmMapper.toResponseDto(testFarm)).willReturn(
                    new FarmResponseDto(FARM_ID, updatedName, updatedLocation, FARM_SURFACE, LocalDate.now(), Collections.emptyList())
            );

            // When
            FarmResponseDto result = farmService.update(testFarmId, request);

            // Then
            assertThat(result.name()).isEqualTo(updatedName);
            assertThat(result.localization()).isEqualTo(updatedLocation);
        }
    }

    @Nested
    class DeleteTests {
        @Test
        void shouldDeleteFarm_WhenFarmExists() {
            // Given
            given(farmRepository.existsById(testFarmId)).willReturn(true);

            // When
            farmService.delete(testFarmId);

            // Then
            verify(farmRepository).deleteById(testFarmId);
        }

        @Test
        void shouldThrowNotFoundException_WhenDeletingNonexistentFarm() {
            // Given
            given(farmRepository.existsById(testFarmId)).willReturn(false);

            // When/Then
            assertThatExceptionOfType(NotFoundException.class)
                    .isThrownBy(() -> farmService.delete(testFarmId))
                    .withMessage("Farm with id " + FARM_ID + " not found");
        }
    }
}