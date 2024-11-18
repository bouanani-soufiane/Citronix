package ma.yc.Citronix.farm.infrastructure.repository;

import ma.yc.Citronix.farm.domain.model.aggregate.Farm;

import java.time.LocalDateTime;
import java.util.List;

public interface FarmCustomRepository {
    List<Farm> search ( String name, String localization, Double surface, LocalDateTime creationDate );

}
