package ma.yc.Citronix.farm;

import ma.yc.Citronix.common.domain.exception.EntityConstraintViolationException;
import ma.yc.Citronix.common.domain.exception.NotFoundException;
import ma.yc.Citronix.farm.application.dto.embeddable.EmbeddedFarmResponseDto;
import ma.yc.Citronix.farm.application.dto.request.create.FieldRequestDto;
import ma.yc.Citronix.farm.application.dto.request.update.FieldUpdateDto;
import ma.yc.Citronix.farm.application.dto.response.FieldResponseDto;
import ma.yc.Citronix.farm.application.mapper.FieldMapper;
import ma.yc.Citronix.farm.domain.model.aggregate.Farm;
import ma.yc.Citronix.farm.domain.model.entity.Field;
import ma.yc.Citronix.farm.domain.model.valueObject.FarmId;
import ma.yc.Citronix.farm.domain.model.valueObject.FieldId;
import ma.yc.Citronix.farm.domain.service.FarmService;
import ma.yc.Citronix.farm.domain.service.FieldService;
import ma.yc.Citronix.farm.domain.service.impl.DefaultFieldService;
import ma.yc.Citronix.farm.infrastructure.repository.FieldRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FieldServiceTest {

    private static final Long FARM_ID = 1L;
    private static final Long FIELD_ID = 1L;
    private static final String FIELD_NAME = "Happy Field";
    private static final Double FIELD_SURFACE = 10.0;
    private static final Farm.FarmBuilder DEFAULT_FARM =
            Farm.builder()
            .id(new FarmId(FARM_ID)).name("Test Farm")
            .localization("Test Location")
            .surface(100.0)
            .creationDate(LocalDate.now());

    @Mock
    private FieldRepository fieldRepository;
    @Mock
    private FieldMapper fieldMapper;
    @Mock
    private FarmService farmService;

    private FieldService fieldService;
    private Farm testFarm;
    private Field testField;

    @BeforeEach
    void setUp () {
        fieldService = new DefaultFieldService(fieldMapper, fieldRepository, farmService);
        testFarm = DEFAULT_FARM.fields(new ArrayList<>()).build();
        testField = createField(FIELD_ID, FIELD_NAME, FIELD_SURFACE, testFarm);
    }

    @Nested
    @DisplayName("Find Field Tests")
    class FindFieldTests {

        @Test
        @DisplayName("Should throw NotFoundException when field doesn't exist")
        void findById_WithNonExistentId_ShouldThrowNotFoundException () {
            FieldId nonExistentId = new FieldId(999L);

            given(fieldRepository.findById(nonExistentId)).willReturn(Optional.empty());

            assertThatThrownBy(() -> fieldService.findById(nonExistentId)).isInstanceOf(NotFoundException.class).hasMessage("Field with id 999 not found");

            verify(fieldRepository).findById(nonExistentId);
            verifyNoMoreInteractions(fieldRepository, fieldMapper);
        }

        @Test
        @DisplayName("Should return field when exists")
        void findById_WithExistingId_ShouldReturnField () {
            FieldResponseDto expectedResponse = createFieldResponse(testField);
            given(fieldRepository.findById(testField.getId())).willReturn(Optional.of(testField));
            given(fieldMapper.toResponseDto(testField)).willReturn(expectedResponse);

            FieldResponseDto actualResponse = fieldService.findById(testField.getId());

            assertThat(actualResponse).usingRecursiveComparison().isEqualTo(expectedResponse);
            verify(fieldRepository).findById(testField.getId());
            verify(fieldMapper).toResponseDto(testField);
        }
    }

    @Nested
    @DisplayName("Create Field Tests")
    class CreateFieldTests {

        @Test
        @DisplayName("Should throw exception when field count exceeds limit")
        void create_WhenFieldCountExceedsLimit_ShouldThrowException () {
            List<Field> existingFields = IntStream.range(0, 10).mapToObj(i -> createField((long) i, "Field " + i, 5.0, testFarm)).toList();
            testFarm.setFields(new ArrayList<>(existingFields));

            FieldRequestDto request = new FieldRequestDto("New Field", 5.0, new FarmId(FARM_ID));
            given(farmService.findEntityById(request.farm())).willReturn(testFarm);

            assertThatThrownBy(() -> fieldService.create(request))
                    .isInstanceOf(EntityConstraintViolationException.class)
                    .hasMessageContaining("A farm cannot have more than 10 fields");

            verify(fieldRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when field surface exceeds limit")
        void create_WhenFieldSurfaceExceedsLimit_ShouldThrowException () {

            double exceedingSurface = testFarm.getSurface() * 0.51;
            FieldRequestDto request = new FieldRequestDto("Large Field", exceedingSurface, new FarmId(FARM_ID));
            given(farmService.findEntityById(request.farm())).willReturn(testFarm);

            assertThatThrownBy(() -> fieldService.create(request))
                    .isInstanceOf(EntityConstraintViolationException.class)
                    .hasMessageContaining("Field surface cannot exceed 50% of the total farm surface");

            verify(fieldRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when total field surface exceeds farm surface")
        void create_WhenTotalFieldSurfaceExceedsFarmSurface_ShouldThrowException () {
            double newFieldSurface = 30.0;

            testFarm.setSurface(100.0);

            List<Field> existingFields = IntStream.range(0, 8).mapToObj(i -> createField((long) i, "Field " + i, 10.0, testFarm)).toList();
            testFarm.setFields(new ArrayList<>(existingFields));

            FieldRequestDto request = new FieldRequestDto("New Field", newFieldSurface, new FarmId(FARM_ID));

            given(farmService.findEntityById(request.farm())).willReturn(testFarm);

            assertThatThrownBy(() -> fieldService.create(request))
                    .isInstanceOf(EntityConstraintViolationException.class)
                    .hasMessageContaining("total surface of all fields cannot exceed the farm's surface");

            verify(fieldRepository, never()).save(any());
        }

    }

    private Field createField ( Long id, String name, Double surface, Farm farm ) {
        return new Field(new FieldId(id), name, surface, farm, List.of());
    }

    private FieldResponseDto createFieldResponse ( Field field ) {
        return new FieldResponseDto(field.getId(), field.getName(), field.getSurface(), createEmbeddedFarmResponse(field.getFarm()));
    }

    private EmbeddedFarmResponseDto createEmbeddedFarmResponse ( Farm farm ) {
        return new EmbeddedFarmResponseDto(farm.getId().value(), farm.getName(), farm.getLocalization(), farm.getSurface(), farm.getCreationDate());
    }
}