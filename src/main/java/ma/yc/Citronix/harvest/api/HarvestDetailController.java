package ma.yc.Citronix.harvest.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.yc.Citronix.harvest.application.dto.request.create.HarvestDetailRequestDto;
import ma.yc.Citronix.harvest.application.dto.response.HarvestDetailResponseDto;
import ma.yc.Citronix.harvest.domain.model.valueObject.HarvestDetailId;
import ma.yc.Citronix.harvest.domain.model.valueObject.HarvestId;
import ma.yc.Citronix.harvest.domain.service.HarvestDetailService;
import ma.yc.Citronix.tree.domain.model.valueObject.TreeId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/harvests")
@RequiredArgsConstructor
public class HarvestDetailController {

    private final HarvestDetailService service;


    @GetMapping("/{harvestId}/{treeId}")
    public ResponseEntity<HarvestDetailResponseDto> findById (
            @PathVariable Long harvestId,
            @PathVariable Long treeId ) {

        HarvestDetailId harvestDetailId = new HarvestDetailId(new HarvestId(harvestId), new TreeId(treeId));
        HarvestDetailResponseDto response = service.findById(harvestDetailId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{harvestId}/{treeId}")
    public ResponseEntity<HarvestDetailResponseDto> create (
            @PathVariable Long harvestId,
            @PathVariable Long treeId,
            @RequestBody @Valid HarvestDetailRequestDto request ) {
        HarvestDetailId harvestDetailId = new HarvestDetailId(new HarvestId(harvestId), new TreeId(treeId));
        HarvestDetailResponseDto response = service.create(harvestDetailId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{harvestId}/{treeId}")
    public ResponseEntity<HarvestDetailResponseDto> update (
            @PathVariable Long harvestId,
            @PathVariable Long treeId,
            @RequestBody @Valid HarvestDetailRequestDto request ) {
        HarvestDetailId harvestDetailId = new HarvestDetailId(
                new HarvestId(harvestId),
                new TreeId(treeId)
        );
        HarvestDetailResponseDto response = service.update(harvestDetailId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{harvestId}/{treeId}")
    public ResponseEntity<Void> delete (
            @PathVariable Long harvestId,
            @PathVariable Long treeId ) {

        HarvestDetailId harvestDetailId = new HarvestDetailId(
                new HarvestId(harvestId),
                new TreeId(treeId)
        );
        service.delete(harvestDetailId);
        return ResponseEntity.noContent().build();
    }
}
