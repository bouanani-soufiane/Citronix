package ma.yc.Citronix.harvest.infrastructure.repository;

import ma.yc.Citronix.harvest.domain.model.aggregate.Harvest;
import ma.yc.Citronix.harvest.domain.model.valueObject.HarvestId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HarvestRepository extends JpaRepository<Harvest, HarvestId> {
}
