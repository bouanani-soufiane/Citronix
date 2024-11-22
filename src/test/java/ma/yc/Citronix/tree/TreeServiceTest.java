package ma.yc.Citronix.tree;

import ma.yc.Citronix.common.domain.exception.EntityConstraintViolationException;
import ma.yc.Citronix.common.domain.exception.NotFoundException;
import ma.yc.Citronix.farm.domain.model.entity.Field;
import ma.yc.Citronix.farm.domain.model.valueObject.FieldId;
import ma.yc.Citronix.farm.domain.service.FieldService;
import ma.yc.Citronix.tree.application.dto.request.create.TreeRequestDto;
import ma.yc.Citronix.tree.application.dto.request.update.TreeUpdateDto;
import ma.yc.Citronix.tree.application.dto.response.TreeResponseDto;
import ma.yc.Citronix.tree.application.mapper.TreeMapper;
import ma.yc.Citronix.tree.domain.model.aggregate.Tree;
import ma.yc.Citronix.tree.domain.model.valueObject.TreeId;
import ma.yc.Citronix.tree.domain.service.impl.DefaultTreeService;
import ma.yc.Citronix.tree.infrastructure.repository.TreeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultTreeServiceTest {

    private static final Long FIELD_ID = 1L;
    private static final Long TREE_ID = 1L;
    private static final double FIELD_SURFACE = 10.0; // 1 hectare

    @Mock
    private TreeRepository treeRepository;
    @Mock
    private TreeMapper treeMapper;
    @Mock
    private FieldService fieldService;

    private DefaultTreeService treeService;
    private Field testField;
    private Tree testTree;

    @BeforeEach
    void setUp() {
        treeService = new DefaultTreeService(treeRepository, treeMapper, fieldService);
        testField = createField(FIELD_ID, "Test Field", FIELD_SURFACE);
        testTree = createTree(TREE_ID, LocalDate.of(2023, Month.APRIL, 15), testField);
    }

    @Nested
    @DisplayName("Create Tree Tests")
    class CreateTreeTests {

        @Test
        @DisplayName("Should throw exception when planting date is outside allowed months")
        void create_WhenPlantingDateOutsideAllowedMonths_ShouldThrowException() {
            LocalDate invalidPlantingDate = LocalDate.of(2023, Month.FEBRUARY, 15);
            TreeRequestDto request = new TreeRequestDto(invalidPlantingDate, new FieldId(FIELD_ID));

            given(fieldService.findEntityById(request.field())).willReturn(testField);

            assertThatThrownBy(() -> treeService.create(request))
                    .isInstanceOf(EntityConstraintViolationException.class)
                    .hasMessageContaining("Trees can only be planted between MARCH and MAY");

            verify(treeRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when tree density exceeds maximum")
        void create_WhenTreeDensityExceedsMaximum_ShouldThrowException() {
            // Simulate field already at max density
            given(fieldService.findEntityById(any(FieldId.class))).willReturn(testField);
            given(treeRepository.countByField(testField)).willReturn(100);

            TreeRequestDto request = new TreeRequestDto(
                    LocalDate.of(2023, Month.APRIL, 15),
                    new FieldId(FIELD_ID)
            );

            assertThatThrownBy(() -> treeService.create(request))
                    .isInstanceOf(EntityConstraintViolationException.class)
                    .hasMessageContaining("Cannot add more trees. Maximum density is 100 trees/ha");

            verify(treeRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should successfully create tree with valid data")
        void create_WithValidData_ShouldSucceed() {
            TreeRequestDto request = new TreeRequestDto(
                    LocalDate.of(2023, Month.APRIL, 15),
                    new FieldId(FIELD_ID)
            );

            given(fieldService.findEntityById(request.field())).willReturn(testField);
            given(treeRepository.countByField(testField)).willReturn(50);

            Tree newTree = createTree(2L, request.plantingDate(), testField);

            given(treeMapper.toEntity(request)).willReturn(newTree);
            given(treeRepository.save(newTree)).willReturn(newTree);

            TreeResponseDto expectedResponse = createTreeResponse(newTree);
            given(treeMapper.toResponseDto(newTree)).willReturn(expectedResponse);

            TreeResponseDto response = treeService.create(request);

            assertThat(response).isNotNull();
            verify(treeRepository).save(newTree);
        }
    }

    @Nested
    @DisplayName("Update Tree Tests")
    class UpdateTreeTests {

        @Test
        @DisplayName("Should successfully update tree planting date within allowed months")
        void update_WithValidPlantingDate_ShouldSucceed() {
            LocalDate newPlantingDate = LocalDate.of(2023, Month.MAY, 15);
            TreeUpdateDto request = new TreeUpdateDto(newPlantingDate, null);

            given(treeRepository.findById(any(TreeId.class))).willReturn(Optional.of(testTree));
            given(treeRepository.save(any(Tree.class))).willReturn(testTree);

            TreeResponseDto expectedResponse = createTreeResponse(testTree);
            given(treeMapper.toResponseDto(testTree)).willReturn(expectedResponse);

            TreeResponseDto response = treeService.update(testTree.getId(), request);

            assertThat(response).isNotNull();
            verify(treeRepository).save(testTree);
        }

        @Test
        @DisplayName("Should throw exception when updating planting date outside allowed months")
        void update_WithPlantingDateOutsideAllowedMonths_ShouldThrowException() {
            LocalDate invalidPlantingDate = LocalDate.of(2023, Month.FEBRUARY, 15);
            TreeUpdateDto request = new TreeUpdateDto(invalidPlantingDate, null);

            given(treeRepository.findById(any(TreeId.class))).willReturn(Optional.of(testTree));

            assertThatThrownBy(() -> treeService.update(testTree.getId(), request))
                    .isInstanceOf(EntityConstraintViolationException.class)
                    .hasMessageContaining("Trees can only be planted between MARCH and MAY");

            verify(treeRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should update tree field with density validation")
        void update_WithNewField_ShouldValidateDensity() {
            Field newField = createField(2L, "New Field", 10.0);
            TreeUpdateDto request = new TreeUpdateDto(null, new FieldId(newField.getId().value()));

            given(treeRepository.findById(any(TreeId.class))).willReturn(Optional.of(testTree));
            given(fieldService.findEntityById(request.field())).willReturn(newField);
            given(treeRepository.countByField(newField)).willReturn(50);
            given(treeRepository.save(any(Tree.class))).willReturn(testTree);

            TreeResponseDto expectedResponse = createTreeResponse(testTree);
            given(treeMapper.toResponseDto(testTree)).willReturn(expectedResponse);

            TreeResponseDto response = treeService.update(testTree.getId(), request);

            assertThat(response).isNotNull();
            verify(treeRepository).save(testTree);
        }

        @Test
        @DisplayName("Should throw exception when new field density exceeds maximum")
        void update_WithNewFieldExceedingDensity_ShouldThrowException() {
            Field newField = createField(2L, "New Field", 10.0);
            TreeUpdateDto request = new TreeUpdateDto(null, new FieldId(newField.getId().value()));

            given(treeRepository.findById(any(TreeId.class))).willReturn(Optional.of(testTree));
            given(fieldService.findEntityById(request.field())).willReturn(newField);
            given(treeRepository.countByField(newField)).willReturn(100);

            assertThatThrownBy(() -> treeService.update(testTree.getId(), request))
                    .isInstanceOf(EntityConstraintViolationException.class)
                    .hasMessageContaining("Cannot add more trees. Maximum density is 100 trees/ha");

            verify(treeRepository, never()).save(any());
        }
    }

    // Helper methods for creating test objects
    private Field createField(Long id, String name, Double surface) {
        Field field = new Field();
        field.setId(new FieldId(id));
        field.setName(name);
        field.setSurface(surface);
        return field;
    }

    private Tree createTree(Long id, LocalDate plantingDate, Field field) {
        Tree tree = new Tree();
        tree.setId(new TreeId(id));
        tree.setPlantingDate(plantingDate);
        tree.setField(field);
        return tree;
    }

    private TreeResponseDto createTreeResponse(Tree tree) {
        return new TreeResponseDto(
                tree.getId(),
                tree.getPlantingDate(),
                tree.getAge(),
                null  // You might want to create a mock FieldResponseDto here
        );
    }
}