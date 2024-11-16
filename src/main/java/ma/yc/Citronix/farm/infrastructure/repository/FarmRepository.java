package ma.yc.Citronix.farm.infrastructure.repository;

import ma.yc.Citronix.farm.domain.model.aggregate.Farm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FarmRepository extends JpaRepository<Farm , Long> {
}
