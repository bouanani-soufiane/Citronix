package ma.yc.Citronix.harvest.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.yc.Citronix.harvest.application.dto.request.create.HarvestRequestDto;
import ma.yc.Citronix.harvest.application.dto.request.update.HarvestUpdateDto;
import ma.yc.Citronix.harvest.application.dto.response.HarvestResponseDto;
import ma.yc.Citronix.harvest.domain.model.valueObject.HarvestId;
import ma.yc.Citronix.harvest.domain.service.HarvestService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/harvests")
@RequiredArgsConstructor
class HarvestController {

    private final HarvestService service;

    @GetMapping
    public ResponseEntity<Page<HarvestResponseDto>> findAll ( @RequestParam(defaultValue = "0") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize ) {
        Page<HarvestResponseDto> harvests = service.findAll(pageNum, pageSize);
        return ResponseEntity.ok(harvests);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HarvestResponseDto> findById ( @PathVariable Long id ) {
        HarvestResponseDto harvest = service.findById(new HarvestId(id));
        return ResponseEntity.status(HttpStatus.OK).body(harvest);
    }

    @PostMapping
    public ResponseEntity<HarvestResponseDto> create ( @Valid @RequestBody HarvestRequestDto request ) {
        HarvestResponseDto harvest = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(harvest);
    }


    @PutMapping("/{id}")
    public ResponseEntity<HarvestResponseDto> update ( @PathVariable Long id, @Valid @RequestBody HarvestUpdateDto request ) {
        HarvestResponseDto updatedHarvest = service.update(new HarvestId(id), request);
        return ResponseEntity.status(HttpStatus.OK).body(updatedHarvest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete ( @PathVariable Long id ) {
        service.delete(new HarvestId(id));
        return ResponseEntity.noContent().build();
    }
}

