package ma.yc.Citronix.sale;

import ma.yc.Citronix.common.domain.exception.NotFoundException;
import ma.yc.Citronix.harvest.domain.model.aggregate.Harvest;
import ma.yc.Citronix.harvest.domain.service.HarvestService;
import ma.yc.Citronix.sale.application.dto.request.SaleRequestDto;
import ma.yc.Citronix.sale.application.dto.request.SaleUpdateDto;
import ma.yc.Citronix.sale.application.dto.response.SaleResponseDto;
import ma.yc.Citronix.sale.application.mapper.SaleMapper;
import ma.yc.Citronix.sale.domain.exception.HarvestAlreadySoldException;
import ma.yc.Citronix.sale.domain.model.aggregate.Sale;
import ma.yc.Citronix.sale.domain.model.valueObject.SaleId;
import ma.yc.Citronix.sale.domain.service.DefaultSaleService;
import ma.yc.Citronix.sale.domain.service.impl.SaleService;
import ma.yc.Citronix.sale.infrastructure.repository.SaleRepository;
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
class SaleServiceTest {
    private static final Long SALE_ID = 1L;
    private static final LocalDate SALE_DATE = LocalDate.now();
    private static final String CLIENT_NAME = "Test Client";
    private static final Double UNIT_PRICE = 100.0;

    @Mock
    private SaleRepository saleRepository;

    @Mock
    private SaleMapper saleMapper;

    @Mock
    private HarvestService harvestService;

    private SaleService saleService;
    private Sale testSale;
    private SaleId testSaleId;
    private Harvest testHarvest;

    @BeforeEach
    void setUp() {
        saleService = new DefaultSaleService(saleRepository, saleMapper, harvestService);
        testSaleId = new SaleId(SALE_ID);
        testHarvest = new Harvest();
        testSale = createTestSale();
    }

    private Sale createTestSale() {
        return Sale.builder()
                .id(testSaleId)
                .date(SALE_DATE)
                .client(CLIENT_NAME)
                .unitPrice(UNIT_PRICE)
                .harvest(testHarvest)
                .build();
    }

    private SaleResponseDto createExpectedResponse(Sale sale) {
        return new SaleResponseDto(
                sale.getId(),
                sale.getDate(),
                sale.getClient(),
                sale.getUnitPrice(),
                null // HarvestResponseDto would be set here in real scenario
        );
    }

    @Nested
    class FindAllTests {
        @Test
        void shouldReturnEmptyPage_WhenNoSalesExist() {
            given(saleRepository.findAll(any(PageRequest.class)))
                    .willReturn(new PageImpl<>(Collections.emptyList()));

            Page<SaleResponseDto> result = saleService.findAll(0, 10);

            assertThat(result).isEmpty();
        }

        @Test
        void shouldReturnPageOfSales_WhenSalesExist() {
            given(saleRepository.findAll(any(PageRequest.class)))
                    .willReturn(new PageImpl<>(List.of(testSale)));
            given(saleMapper.toResponseDto(testSale))
                    .willReturn(createExpectedResponse(testSale));

            Page<SaleResponseDto> result = saleService.findAll(0, 10);

            assertThat(result).hasSize(1);
            assertThat(result.getContent().get(0).client()).isEqualTo(CLIENT_NAME);
        }
    }

    @Nested
    class FindByIdTests {
        @Test
        void shouldThrowNotFoundException_WhenSaleDoesNotExist() {
            given(saleRepository.findById(testSaleId)).willReturn(Optional.empty());

            assertThatExceptionOfType(NotFoundException.class)
                    .isThrownBy(() -> saleService.findById(testSaleId));
        }

        @Test
        void shouldReturnSale_WhenSaleExists() {
            given(saleRepository.findById(testSaleId)).willReturn(Optional.of(testSale));
            given(saleMapper.toResponseDto(testSale)).willReturn(createExpectedResponse(testSale));

            SaleResponseDto result = saleService.findById(testSaleId);

            assertThat(result.client()).isEqualTo(CLIENT_NAME);
            assertThat(result.unitPrice()).isEqualTo(UNIT_PRICE);
        }
    }

    @Nested
    class CreateTests {
        @Test
        void shouldThrowException_WhenHarvestAlreadySold() {
            SaleRequestDto request = new SaleRequestDto(SALE_DATE, CLIENT_NAME, UNIT_PRICE, testHarvest.getId());
            given(saleRepository.existsByHarvestId(any())).willReturn(true);

            assertThatExceptionOfType(HarvestAlreadySoldException.class)
                    .isThrownBy(() -> saleService.create(request));
        }

        @Test
        void shouldCreateAndReturnSale_WhenValidRequest() {
            SaleRequestDto request = new SaleRequestDto(SALE_DATE, CLIENT_NAME, UNIT_PRICE, testHarvest.getId());
            given(saleRepository.existsByHarvestId(any())).willReturn(false);
            given(harvestService.findEntityById(any())).willReturn(testHarvest);
            given(saleMapper.toEntity(request)).willReturn(testSale);
            given(saleRepository.save(any(Sale.class))).willReturn(testSale);
            given(saleMapper.toResponseDto(testSale)).willReturn(createExpectedResponse(testSale));

            SaleResponseDto result = saleService.create(request);

            assertThat(result.client()).isEqualTo(CLIENT_NAME);
            assertThat(result.unitPrice()).isEqualTo(UNIT_PRICE);
        }
    }

    @Nested
    class UpdateTests {
        @Test
        void shouldThrowNotFoundException_WhenUpdatingNonexistentSale() {
            SaleUpdateDto request = new SaleUpdateDto(SALE_DATE, "Updated Client", UNIT_PRICE, testHarvest.getId());
            given(saleRepository.findById(testSaleId)).willReturn(Optional.empty());

            assertThatExceptionOfType(NotFoundException.class)
                    .isThrownBy(() -> saleService.update(testSaleId, request));
        }

        @Test
        void shouldUpdateAndReturnSale_WhenValidRequest() {
            String updatedClient = "Updated Client";
            SaleUpdateDto request = new SaleUpdateDto(SALE_DATE, updatedClient, UNIT_PRICE, testHarvest.getId());

            given(saleRepository.findById(testSaleId)).willReturn(Optional.of(testSale));
            given(saleRepository.save(any(Sale.class))).willReturn(testSale);
            given(saleMapper.toResponseDto(any(Sale.class)))
                    .willReturn(new SaleResponseDto(testSaleId, SALE_DATE, updatedClient, UNIT_PRICE, null));

            SaleResponseDto result = saleService.update(testSaleId, request);

            assertThat(result.client()).isEqualTo(updatedClient);
        }
    }

    @Nested
    class DeleteTests {
        @Test
        void shouldDeleteSale_WhenSaleExists() {
            given(saleRepository.existsById(testSaleId)).willReturn(true);

            saleService.delete(testSaleId);

            verify(saleRepository).deleteById(testSaleId);
        }

        @Test
        void shouldThrowNotFoundException_WhenDeletingNonexistentSale() {
            given(saleRepository.existsById(testSaleId)).willReturn(false);

            assertThatExceptionOfType(NotFoundException.class)
                    .isThrownBy(() -> saleService.delete(testSaleId));
        }
    }
}