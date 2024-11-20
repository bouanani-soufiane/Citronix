package ma.yc.Citronix.harvest.infrastructure.repository;

import ma.yc.Citronix.farm.domain.model.valueObject.FarmId;
import ma.yc.Citronix.harvest.domain.model.aggregate.Harvest;
import ma.yc.Citronix.harvest.domain.model.enums.Season;
import ma.yc.Citronix.harvest.domain.model.valueObject.HarvestId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface HarvestRepository extends JpaRepository<Harvest, HarvestId> {
    @Query("SELECT COUNT(h) > 0 FROM Harvest h " +
            "WHERE h.farm.id = :id " +
            "AND h.season = :season " +
            "AND YEAR(h.date) = :year")
    boolean existsByFarmIdAndSeasonAndYear(FarmId id, Season season, int year);
}
