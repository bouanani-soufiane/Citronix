package ma.yc.Citronix.tree.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.yc.Citronix.tree.application.dto.request.create.TreeRequestDto;
import ma.yc.Citronix.tree.application.dto.request.update.TreeUpdateDto;
import ma.yc.Citronix.tree.application.dto.response.TreeResponseDto;
import ma.yc.Citronix.tree.domain.model.valueObject.TreeId;
import ma.yc.Citronix.tree.domain.service.TreeService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/trees")
@RequiredArgsConstructor
class TreeController {

    private final TreeService service;

    @GetMapping
    public ResponseEntity<Page<TreeResponseDto>> findAll (
            @RequestParam(defaultValue = "0") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        Page<TreeResponseDto> trees = service.findAll(pageNum, pageSize);
        return ResponseEntity.ok(trees);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TreeResponseDto> findById ( @PathVariable Long id ) {
        TreeResponseDto farm = service.findById(new TreeId(id));
        return ResponseEntity.status(HttpStatus.OK).body(farm);
    }

    @PostMapping
    public ResponseEntity<TreeResponseDto> create ( @Valid @RequestBody TreeRequestDto request ) {
        TreeResponseDto tree = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(tree);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TreeResponseDto> update (
            @PathVariable Long id,
            @Valid @RequestBody TreeUpdateDto request
    ) {
        TreeResponseDto updatedTree = service.update(new TreeId(id), request);
        return ResponseEntity.status(HttpStatus.OK).body(updatedTree);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete ( @PathVariable Long id ) {
        service.delete(new TreeId(id));
        return ResponseEntity.noContent().build();
    }


}
