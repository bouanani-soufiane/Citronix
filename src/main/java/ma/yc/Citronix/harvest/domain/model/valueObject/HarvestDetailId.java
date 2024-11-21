package ma.yc.Citronix.harvest.domain.model.valueObject;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import ma.yc.Citronix.tree.domain.model.valueObject.TreeId;

@Embeddable
public record HarvestDetailId(
        @AttributeOverride(name = "value", column = @Column(name = "harvest_id"))
        HarvestId harvestId,
        @AttributeOverride(name = "value", column = @Column(name = "tree_id"))
        TreeId treeId) {
}