package ma.yc.Citronix.harvest.domain.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.yc.Citronix.common.domain.exception.EntityConstraintViolationException;
import ma.yc.Citronix.common.domain.exception.NotFoundException;
import ma.yc.Citronix.harvest.application.dto.request.create.HarvestDetailRequestDto;
import ma.yc.Citronix.harvest.application.dto.response.HarvestDetailResponseDto;
import ma.yc.Citronix.harvest.application.mapper.HarvestDetailMapper;
import ma.yc.Citronix.harvest.domain.model.aggregate.Harvest;
import ma.yc.Citronix.harvest.domain.model.aggregate.HarvestDetail;
import ma.yc.Citronix.harvest.domain.model.valueObject.HarvestDetailId;
import ma.yc.Citronix.harvest.domain.service.HarvestDetailService;
import ma.yc.Citronix.harvest.domain.service.HarvestService;
import ma.yc.Citronix.harvest.infrastructure.repository.HarvestDetailRepository;
import ma.yc.Citronix.tree.domain.model.aggregate.Tree;
import ma.yc.Citronix.tree.domain.service.TreeService;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DefaultHarvestDetailService implements HarvestDetailService {

    private final HarvestDetailRepository repository;
    private final HarvestDetailMapper mapper;
    private final HarvestService harvestService;
    private final TreeService treeService;

    @Override
    public HarvestDetailResponseDto create ( HarvestDetailId id, HarvestDetailRequestDto dto ) {

        Harvest harvest = harvestService.findEntityById(id.harvestId());
        Tree tree = treeService.findTreeById(id.treeId());

        if(repository.existsByHarvestIdAndTreeId(harvest.getId(),tree.getId()))
            throw new EntityConstraintViolationException("Harvest" , "tree" , tree.getId().value(),"already been harvested for the specified harvest");

        validateQttByTreeAge(tree.getAge(), dto.quantity());
        HarvestDetail savedDetail = repository.save(
                new HarvestDetail(harvest, tree, dto.date(), dto.quantity())
        );
        harvest.addHarvestDetail(savedDetail);
        return mapper.toResponseDto(savedDetail);
    }

    @Override
    public HarvestDetailResponseDto findById ( HarvestDetailId id ) {
        return repository.findById(id)
                .map(mapper::toResponseDto)
                .orElseThrow(() -> new NotFoundException("Harvest Details" , id));
    }

    @Override
    public HarvestDetailResponseDto update(HarvestDetailId id, HarvestDetailRequestDto dto) {
        HarvestDetail harvestDetail = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Harvest Details" , id));

        Tree tree = treeService.findTreeById(id.treeId());
        validateQttByTreeAge(tree.getAge(), dto.quantity());

        harvestDetail.setQuantity(dto.quantity()).setDate(dto.date());


        return mapper.toResponseDto(harvestDetail);
    }


    @Override
    public void delete ( HarvestDetailId id ) {
        if (!repository.existsById(id))
         throw new NotFoundException ("Harvest Details" , id);

        repository.deleteById(id);
    }

    public boolean validateQttByTreeAge ( int age, double quantity ) {
        if (age < 3 && quantity > 2.5) {
            throw new EntityConstraintViolationException(
                    "Tree",
                    "quantity",
                    quantity,
                    "Quantity exceeds the limit of 2.5 kg for young trees (< 3 years old)."
            );
        }
        if (age >= 3 && age <= 10 && quantity > 12) {
            throw new EntityConstraintViolationException(
                    "Tree",
                    "quantity",
                    quantity,
                    "Quantity exceeds the limit of 12 kg for mature trees (3-10 years old)."
            );
        }
        if (age > 10 && quantity > 20) {
            throw new EntityConstraintViolationException(
                    "Tree",
                    "quantity",
                    quantity,
                    "Quantity exceeds the limit of 20 kg for old trees (> 10 years old)."
            );
        }
        if (age > 20 && quantity > 0) {
            throw new EntityConstraintViolationException(
                    "Tree",
                    "quantity",
                    quantity,
                    "Tree with age > 20 considered as unproductive"
            );
        }

        return true;
    }
}