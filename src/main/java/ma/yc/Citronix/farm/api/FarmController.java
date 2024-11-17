package ma.yc.Citronix.farm.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.yc.Citronix.farm.application.dto.request.FarmRequestDto;
import ma.yc.Citronix.farm.application.dto.response.FarmResponseDto;
import ma.yc.Citronix.farm.domain.model.valueObject.FarmId;
import ma.yc.Citronix.farm.domain.service.FarmService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/farms")
@RequiredArgsConstructor
class FarmController {

    private final FarmService service;


    @GetMapping
    public ResponseEntity<Page<FarmResponseDto>> findAll (
            @RequestParam(defaultValue = "0") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        Page<FarmResponseDto> farms = service.findAll(pageNum, pageSize);
        return ResponseEntity.ok(farms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FarmResponseDto> findById ( @PathVariable Long id ) {
        FarmResponseDto farm = service.findById(new FarmId(id));
        return ResponseEntity.status(HttpStatus.OK).body(farm);
    }


    @PostMapping
    public ResponseEntity<FarmResponseDto> create ( @Valid @RequestBody FarmRequestDto request ) {
        FarmResponseDto farm = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(farm);

    }

    @PutMapping("/{id}")
    public ResponseEntity<FarmResponseDto> update ( @PathVariable Long id, @Valid @RequestBody FarmRequestDto request ) {
        FarmResponseDto updatedFarm = service.update(new FarmId(id), request);
        return ResponseEntity.status(HttpStatus.OK).body(updatedFarm);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete ( @PathVariable Long id ) {
        service.delete(new FarmId(id));
        return ResponseEntity.noContent().build();
    }
}

