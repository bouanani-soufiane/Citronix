package ma.yc.Citronix.sale.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.yc.Citronix.sale.application.dto.request.SaleRequestDto;
import ma.yc.Citronix.sale.application.dto.response.SaleResponseDto;
import ma.yc.Citronix.sale.domain.model.valueObject.SaleId;
import ma.yc.Citronix.sale.domain.service.impl.SaleService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sales")
@RequiredArgsConstructor
class SaleController {

    private final SaleService service;



    @GetMapping
    public ResponseEntity<Page<SaleResponseDto>> findAll (
            @RequestParam(defaultValue = "0") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        Page<SaleResponseDto> sales = service.findAll(pageNum, pageSize);
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleResponseDto> findById ( @PathVariable Long id ) {
        SaleResponseDto sale = service.findById(new SaleId(id));
        return ResponseEntity.status(HttpStatus.OK).body(sale);
    }


    @PostMapping
    public ResponseEntity<SaleResponseDto> create ( @Valid @RequestBody SaleRequestDto request ) {
        SaleResponseDto sale = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(sale);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete ( @PathVariable Long id ) {
        service.delete(new SaleId(id));
        return ResponseEntity.noContent().build();
    }


}
