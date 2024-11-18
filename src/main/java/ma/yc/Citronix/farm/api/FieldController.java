package ma.yc.Citronix.farm.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.yc.Citronix.farm.application.dto.request.create.FieldRequestDto;
import ma.yc.Citronix.farm.application.dto.response.FieldResponseDto;
import ma.yc.Citronix.farm.domain.model.valueObject.FarmId;
import ma.yc.Citronix.farm.domain.model.valueObject.FieldId;
import ma.yc.Citronix.farm.domain.service.FieldService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/fields")
@RequiredArgsConstructor
class FieldController {

    private final FieldService service;

    @GetMapping
    public ResponseEntity<Page<FieldResponseDto>> findAll (
            @RequestParam(defaultValue = "0") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        Page<FieldResponseDto> fields = service.findAll(pageNum, pageSize);
        return ResponseEntity.ok(fields);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FieldResponseDto> findById ( @PathVariable Long id ) {
        FieldResponseDto field = service.findById(new FieldId(id));
        return ResponseEntity.status(HttpStatus.OK).body(field);
    }

    @PostMapping
    public ResponseEntity<FieldResponseDto> create( @Valid @RequestBody FieldRequestDto request){
        FieldResponseDto field = service.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(field);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete ( @PathVariable Long id ) {
        service.delete(new FieldId(id));
        return ResponseEntity.noContent().build();
    }
}
