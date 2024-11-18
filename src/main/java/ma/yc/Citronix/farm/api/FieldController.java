package ma.yc.Citronix.farm.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.yc.Citronix.farm.application.dto.request.FieldRequestDto;
import ma.yc.Citronix.farm.application.dto.response.FieldResponseDto;
import ma.yc.Citronix.farm.domain.model.entity.Field;
import ma.yc.Citronix.farm.domain.service.FieldService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/fields")
@RequiredArgsConstructor
class FieldController {

    private final FieldService service;

    @PostMapping
    public ResponseEntity<FieldResponseDto> create( @Valid @RequestBody FieldRequestDto request){
        FieldResponseDto field = service.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(field);
    }
}
