package ma.yc.Citronix.farm.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.yc.Citronix.farm.application.dto.request.FarmRequestDto;
import ma.yc.Citronix.farm.application.dto.response.FarmResponseDto;
import ma.yc.Citronix.farm.domain.service.FarmService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/farms")
@RequiredArgsConstructor
class FarmController {

    private final FarmService service;

    @PostMapping
    public ResponseEntity<FarmResponseDto> create ( @Valid @RequestBody FarmRequestDto request ) {
        FarmResponseDto farm = service.create(request);
        return ResponseEntity.ok(farm);

    }
}
