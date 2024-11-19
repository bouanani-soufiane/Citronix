package ma.yc.Citronix.tree.infrastructure.repository;

import ma.yc.Citronix.farm.domain.model.entity.Field;
import ma.yc.Citronix.tree.domain.model.aggregate.Tree;
import ma.yc.Citronix.tree.domain.model.valueObject.TreeId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TreeRepository extends JpaRepository<Tree, TreeId> {
    int countByField ( Field field );
}
