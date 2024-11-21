package ma.yc.Citronix.harvest.infrastructure.repository;

import ma.yc.Citronix.harvest.domain.model.aggregate.HarvestDetail;
import ma.yc.Citronix.harvest.domain.model.valueObject.HarvestDetailId;
import ma.yc.Citronix.harvest.domain.model.valueObject.HarvestId;
import ma.yc.Citronix.tree.domain.model.valueObject.TreeId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HarvestDetailRepository extends JpaRepository<HarvestDetail, HarvestDetailId> {
    boolean existsByHarvestIdAndTreeId ( HarvestId harvestId , TreeId treeId );
}
