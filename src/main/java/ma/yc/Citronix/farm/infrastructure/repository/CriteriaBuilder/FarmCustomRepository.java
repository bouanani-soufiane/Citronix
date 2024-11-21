package ma.yc.Citronix.farm.infrastructure.repository.CriteriaBuilder;

import ma.yc.Citronix.farm.domain.model.aggregate.Farm;

import java.time.LocalDate;
import java.util.List;

public interface FarmCustomRepository {
    List<Farm> search ( String name, String localization, Double surface, LocalDate creationDate );

}
