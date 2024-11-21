package ma.yc.Citronix.sale.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.yc.Citronix.sale.application.dto.request.SaleRequestDto;
import ma.yc.Citronix.sale.application.dto.response.SaleResponseDto;
import ma.yc.Citronix.sale.domain.service.impl.SaleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sales")
@RequiredArgsConstructor
class SaleController {

    private final SaleService service;

    @PostMapping
    public ResponseEntity<SaleResponseDto> create ( @Valid @RequestBody SaleRequestDto request ) {
        SaleResponseDto sale = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(sale);
    }


}
