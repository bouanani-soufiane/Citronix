package ma.yc.Citronix.harvest.infrastructure.repository;

import ma.yc.Citronix.harvest.domain.model.aggregate.HarvestDetail;
import ma.yc.Citronix.harvest.domain.model.valueObject.HarvestDetailId;
import ma.yc.Citronix.harvest.domain.model.valueObject.HarvestId;
import ma.yc.Citronix.tree.domain.model.valueObject.TreeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HarvestDetailRepository extends JpaRepository<HarvestDetail, HarvestDetailId> {
    boolean existsByHarvestIdAndTreeId ( HarvestId harvestId , TreeId treeId );

    @Query("SELECT f.name, SUM(hd.quantity) FROM HarvestDetail hd " +
            "JOIN hd.tree t " +
            "JOIN t.field f " +
            "GROUP BY f.name")
    List<Object[]> findTotalQuantityByFieldName();
    ;

}
