package ma.yc.Citronix.sale.domain.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.yc.Citronix.common.domain.exception.NotFoundException;
import ma.yc.Citronix.sale.application.dto.request.SaleRequestDto;
import ma.yc.Citronix.sale.application.dto.request.SaleUpdateDto;
import ma.yc.Citronix.sale.application.dto.response.SaleResponseDto;
import ma.yc.Citronix.sale.application.mapper.SaleMapper;
import ma.yc.Citronix.sale.domain.model.aggregate.Sale;
import ma.yc.Citronix.sale.domain.model.valueObject.SaleId;
import ma.yc.Citronix.sale.domain.service.impl.SaleService;
import ma.yc.Citronix.sale.infrastructure.repository.SaleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DefaultSaleService implements SaleService {
    private final SaleRepository repository;
    private final SaleMapper mapper;


    @Override
    public Page<SaleResponseDto> findAll ( int pageNum, int pageSize ) {
        return repository.findAll(PageRequest.of(pageNum, pageSize))
                .map(mapper::toResponseDto);
    }

    @Override
    public SaleResponseDto findById ( SaleId id ) {
        return mapper.toResponseDto(repository.findById(id).orElseThrow(() -> new NotFoundException("Field", id.value())));

    }

    @Override
    public SaleResponseDto create ( SaleRequestDto dto ) {

        return mapper.toResponseDto(repository.save(mapper.toEntity(dto)));
    }

    @Override
    public SaleResponseDto update(SaleId id, SaleUpdateDto dto) {
        Sale sale = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sale", id.value()));

        sale.setDate(dto.date());
        sale.setClient(dto.client());
        sale.setUnitPrice(dto.unitPrice());

        Sale updatedSale = repository.save(sale);

        return mapper.toResponseDto(updatedSale);
    }

    @Override
    public void delete ( SaleId id ) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Sale", id.value());
        }

        repository.deleteById(id);
    }
}
