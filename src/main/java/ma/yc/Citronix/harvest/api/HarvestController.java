package ma.yc.Citronix.harvest.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.yc.Citronix.harvest.application.dto.request.create.HarvestRequestDto;
import ma.yc.Citronix.harvest.application.dto.response.HarvestResponseDto;
import ma.yc.Citronix.harvest.domain.service.HarvestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/harvests")
@RequiredArgsConstructor
class HarvestController {

    private final HarvestService service;


    @PostMapping
    public ResponseEntity<HarvestResponseDto> create ( @Valid @RequestBody HarvestRequestDto request ) {
        HarvestResponseDto harvest = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(harvest);
    }
}
