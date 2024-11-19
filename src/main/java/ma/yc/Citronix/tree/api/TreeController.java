package ma.yc.Citronix.tree.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.yc.Citronix.farm.domain.model.valueObject.FieldId;
import ma.yc.Citronix.tree.application.dto.request.create.TreeRequestDto;
import ma.yc.Citronix.tree.application.dto.response.TreeResponseDto;
import ma.yc.Citronix.tree.domain.model.valueObject.TreeId;
import ma.yc.Citronix.tree.domain.service.TreeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/trees")
@RequiredArgsConstructor
class TreeController {

    private final TreeService service;

    @PostMapping
    public ResponseEntity<TreeResponseDto> create ( @Valid @RequestBody TreeRequestDto request ) {
        TreeResponseDto tree = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(tree);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete ( @PathVariable Long id ) {
        service.delete(new TreeId(id));
        return ResponseEntity.noContent().build();
    }

}
